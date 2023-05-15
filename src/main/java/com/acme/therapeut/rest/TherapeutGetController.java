package com.acme.therapeut.rest;

import com.acme.therapeut.entity.Therapeut;
import com.acme.therapeut.service.TherapeutReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.UUID;

import static com.acme.therapeut.rest.TherapeutGetController.REST_PATH;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Eine @RestController-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Methoden der Klasse abgebildet werden.
 *
 * @author Valentin Sackmann
 */
@Controller
@RequestMapping(REST_PATH)
@ResponseBody
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("TrailingComment")
public class TherapeutGetController {
    /**
     * Basispfad für die REST-Schnittstelle.
     */
    public static final String REST_PATH = "/rest";


    /**
     * Muster für eine UUID. [\dA-Fa-f]{8}{8}-([\dA-Fa-f]{8}{4}-){3}[\dA-Fa-f]{8}{12} enthält eine "capturing group"
     * und ist nicht zulässig.
     */
    public static final String ID_PATTERN =
        "[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}";

    /**
     * Pfad, um Nachnamen abzufragen.
     */
    private final TherapeutReadService service;

    private final UriHelper uriHelper;


    /**
     * Finde alle Therapeuten.
     *
     * @return gefundene Therapeuten oder leere collection.
     */
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Therapeut gefunden")
    Collection<Therapeut> findAll() {
        return service.findAll();
    }

    /**
     * Suche mit diversen Suchkriterien als Query-Parameter.
     *
     * @param suchkriterien Query-Parameter als Map.
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Gefundenen Therapeuten als CollectionModel.
     */
    @GetMapping(produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "CollectionModel mid den Therapeuten")
    @ApiResponse(responseCode = "404", description = "Keine Therapeuten gefunden")
    CollectionModel<TherapeutModel> find(
        @RequestParam @NonNull final MultiValueMap<String, String> suchkriterien,
        final HttpServletRequest request
    ) {
        log.debug("find: suchkriterien={}", suchkriterien);

        final var baseUri = uriHelper.getBaseUri(request).toString();

        // Geschaeftslogik
        final var models = service.find(suchkriterien)
            .stream()
            .map(therapeut -> {
                final var model = new TherapeutModel(therapeut);
                model.add(Link.of(baseUri + '/' + therapeut.getId()));
                return model;
            })
            .toList();

        log.debug("find: {}", models);
        return CollectionModel.of(models);
    }
}
