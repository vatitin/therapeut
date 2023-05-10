package com.acme.therapeut.graphql;

import com.acme.therapeut.entity.Therapeut;
import com.acme.therapeut.service.TherapeutReadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyMap;

/**
 * Eine Controller-Klasse für das Lesen mit der GraphQL-Schnittstelle und den Typen aus dem GraphQL-Schema.
 *
 * @author Valentin Sackmann
 */
@Controller
@RequiredArgsConstructor
@Slf4j
class TherapeutQueryController {
    private final TherapeutReadService service;

    /**
     * Suche anhand der Therapeut-ID.
     *
     * @param id ID des zu suchenden Therapeuten
     * @return Der gefundene Therapeut
     */
    @QueryMapping
    Therapeut therapeut(@Argument final UUID id) {
        log.debug("therapeut: id={}", id);
        final var therapeut = service.findById(id);
        log.debug("therapeut: {}", therapeut);
        return therapeut;
    }

    /**
     * Suche mit diversen Suchkriterien.
     *
     * @param input Suchkriterien und ihre Werte, z.B. `nachname` und `Max`
     * @return Die gefundenen Therapeuten als Collection
     */
    @QueryMapping
    Collection<Therapeut> therapeuten(@Argument final Optional<Suchkriterien> input) {
        log.debug("therapeuten: suchkriterien={}", input);
        final var suchkriterien = input.map(Suchkriterien::toMap).orElse(emptyMap());
        final var therapeuten = service.find(suchkriterien);
        log.debug("therapeuten: {}", therapeuten);
        return therapeuten;
    }
}
