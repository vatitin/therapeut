package com.acme.therapeut.graphql;

import com.acme.therapeut.entity.Therapeut;
import com.acme.therapeut.service.ConstraintViolationsException;
import com.acme.therapeut.service.EmailExistsException;
import com.acme.therapeut.service.TherapeutWriteService;
import graphql.GraphQLError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.graphql.execution.ErrorType.BAD_REQUEST;

/**
 * Eine Controller-Klasse für das Schreiben mit der GraphQL-Schnittstelle und den Typen aus dem GraphQL-Schema.
 *
 * @author Valentin Sackmann
 */
@Controller
@RequiredArgsConstructor
@Slf4j
class TherapeutMutationController {
    private final TherapeutWriteService service;

    /**
     * Einen neuen Therapeuten anlegen.
     *
     * @param input Die Eingabedaten für einen neuen Therapeuten
     * @return Die generierte ID für den neuen Therapeuten als Payload
     */
    @MutationMapping
    CreatePayload create(@Argument final TherapeutInput input) {
        log.debug("create: input={}", input);
        final var id = service.create(input.toTherapeut()).getId();
        log.debug("create: id={}", id);
        return new CreatePayload(id);
    }

    @GraphQlExceptionHandler
    GraphQLError handleEmailExists(final EmailExistsException ex) {
        return GraphQLError.newError()
            .errorType(BAD_REQUEST)
            .message("Die Emailadresse " + ex.getEmail() + " existiert bereits.")
            .build();
    }

    @GraphQlExceptionHandler
    GraphQLError handleDateTimeParseException(final DateTimeParseException ex) {
        return GraphQLError.newError()
            .errorType(BAD_REQUEST)
            .message("Das Datum " + ex.getParsedString() + " ist nicht korrekt.")
            .build();
    }

    @GraphQlExceptionHandler
    Collection<GraphQLError> handleConstraintViolations(final ConstraintViolationsException ex) {
        return ex.getViolations()
            .stream()
            .map(this::violationToGraphQLError)
            .collect(Collectors.toList());
    }

    private GraphQLError violationToGraphQLError(final ConstraintViolation<Therapeut> violation) {
        final List<Object> path = new ArrayList<>(5);
        path.add("input");
        for (final Path.Node node: violation.getPropertyPath()) {
            path.add(node.toString());
        }
        return GraphQLError.newError()
            .errorType(BAD_REQUEST)
            .message(violation.getMessage())
            .path(path)
            .build();
    }
}
