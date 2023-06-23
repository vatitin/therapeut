package com.acme.therapeut.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import static org.springframework.http.HttpHeaders.IF_NONE_MATCH;

/**
 * "HTTP Interface" für den REST-Client für Mitglieddaten.
 *
 * @author Valentin Sackmann
 */
@HttpExchange("/rest")
public interface MitgliedRestRepository {
    /**
     * Einen Kundendatensatz vom Microservice "mitglied" mit "Basic Authentication" anfordern.
     *
     * @param id ID des angeforderten Mitglieds
     * @param version Version des angeforderten Datensatzes
     * @return Gefundenes Mitglied
     */
    @GetExchange("/{id}")
    @SuppressWarnings("unused")
    ResponseEntity<Mitglied> getMitgliedMitVersion(
        @PathVariable String id,
        @RequestHeader(IF_NONE_MATCH) String version
    );

    /**
     * Einen Mitglieddatensatz vom Microservice "mitglied" mit "Basic Authentication" anfordern.
     *
     * @param id ID des angeforderten Mitglieds
     * @return Gefundenes Mitglied
     */
    @GetExchange("/{id}")
    ResponseEntity<Mitglied> getMitglied(
        @PathVariable String id
    );
}
