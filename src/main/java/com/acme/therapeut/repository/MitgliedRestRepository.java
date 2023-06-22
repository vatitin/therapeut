package com.acme.therapeut.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.IF_NONE_MATCH;

/**
 * "HTTP Interface" für den REST-Client für Kundedaten.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@HttpExchange("/rest")
public interface MitgliedRestRepository {
    /**
     * Einen Kundendatensatz vom Microservice "kunde" mit "Basic Authentication" anfordern.
     *
     * @param id ID des angeforderten Kunden
     * @param version Version des angeforderten Datensatzes
     * @return Gefundener Kunde
     */
    @GetExchange("/{id}")
    @SuppressWarnings("unused")
    ResponseEntity<Mitglied> getMitgliedMitVersion(
        @PathVariable String id,
        @RequestHeader(IF_NONE_MATCH) String version
    );

    /**
     * Einen Kundendatensatz vom Microservice "kunde" mit "Basic Authentication" anfordern.
     *
     * @param id ID des angeforderten Kunden
     * @param authorization String für den HTTP-Header "Authorization"
     * @return Gefundener Kunde
     */
    @GetExchange("/{id}")
    ResponseEntity<Mitglied> getMitglied(
        @PathVariable String id
    );
}
