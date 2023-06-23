package com.acme.therapeut.service;

import lombok.Getter;

/**
 * Exception, falls die Versionsnummer nicht aktuell ist.
 *
 * @author Valentin Sackmann
 */
@Getter
public class VersionOutdatedException extends RuntimeException {
    private final int version;

    VersionOutdatedException(final int version) {
        super("Die Versionsnummer " + version + " ist veraltet.");
        this.version = version;
    }
}
