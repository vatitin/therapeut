package com.acme.therapeut.rest;

import com.acme.therapeut.entity.Therapeut;
import com.acme.therapeut.service.TherapeutReadService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.UUID;

import static com.acme.therapeut.rest.TherapeutGetController.REST_PATH;
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


    /**
     * Suche anhand der Therapeut-ID als Pfad-Parameter.
     *
     * @param id ID des zu suchenden Therapeuten
     * @return Gefundener Therapeut.
     */
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Therapeut gefunden")
    Therapeut findById(@PathVariable final UUID id) {
        return service.findById(id);
    }


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
}
