package com.acme.therapeut.rest;

/**
 * Enum für ProblemDetail.type.
 *
 * @author Valentin Sackmann
 */
enum ProblemType {
    /**
     * Constraints als Fehlerursache.
     */
    CONSTRAINTS("constraints"),

    /**
     * Fehler beim Header If-Match.
     */
    PRECONDITION("precondition"),

    /**
     * Fehler, wenn z.B. Emailadresse bereits existiert.
     */
    UNPROCESSABLE("unprocessable");

    private final String value;

    ProblemType(final String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }
}
