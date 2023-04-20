package com.acme.therapeut.service;

import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * RuntimeException, falls kein Therapeut gefunden wurde.
 */
@Getter
public final class NotFoundException extends RuntimeException {
    /**
     * Nicht-vorhandene ID.
     */
    private final UUID id;

    /**
     * Suchkriterien, zu denen nichts gefunden wurde.
     */
    private final Map<String, List<String>> suchkriterien;

    NotFoundException(final UUID id) {
        super("Kein Therapeut mit der ID " + id + " gefunden.");
        this.id = id;
        suchkriterien = null;
    }


    NotFoundException() {
        super("Keine Therapeuten gefunden.");
        id = null;
        suchkriterien = null;
    }
}
