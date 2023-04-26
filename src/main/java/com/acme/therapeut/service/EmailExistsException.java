package com.acme.therapeut.service;

import lombok.Getter;

/**
 * Exception, falls die Emailadresse bereits existiert.
 *
 * @author Valentin Sackmann
 */
@Getter
public class EmailExistsException extends RuntimeException {
    /**
     * Bereits vorhandene Emailadresse.
     */
    private final String email;

    EmailExistsException(@SuppressWarnings("ParameterHidesMemberVariable") final String email) {
        super("Die Emailadresse " + email + " existiert bereits");
        this.email = email;
    }
}
