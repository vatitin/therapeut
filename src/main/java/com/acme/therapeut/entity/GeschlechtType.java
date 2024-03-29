package com.acme.therapeut.entity;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;
import java.util.stream.Stream;

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

    /**
     * Konvertierung eines Strings in einen Enum-Wert.
     *
     * @param value Der String, zu dem ein passender Enum-Wert ermittelt werden soll.
     * @return Passender Enum-Wert oder null.
     */
    public static Optional<GeschlechtType> of(final String value) {
        return Stream.of(values())
            .filter(geschlecht -> geschlecht.value.equalsIgnoreCase(value))
            .findFirst();
    }
}
