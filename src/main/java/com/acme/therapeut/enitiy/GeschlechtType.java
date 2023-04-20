package com.acme.therapeut.enitiy;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Enum für Geschlecht.
 *
 * @author Valentin Sackmann
 */
public enum GeschlechtType {
    /**
     * _Männlich_ mit dem internen Wert `M`.
     */
    MAENNLICH("M"),

    /**
     * _Weiblich_ mit dem internen Wert `W`.
     */
    WEIBLICH("W"),

    /**
     * _Divers_ mit dem internen Wert `D`.
     */
    DIVERS("D");

    private final String value;

    GeschlechtType(final String value) {
        this.value = value;
    }

    /**
     * Einen enum-Wert als String mit dem internen Wert ausgeben.
     *
     * @return Interner Wert
     */
    @JsonValue
    @Override
    public String toString() {
        return value;
    }

}
