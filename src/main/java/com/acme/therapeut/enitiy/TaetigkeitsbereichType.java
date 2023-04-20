package com.acme.therapeut.enitiy;


import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enum für Taetigkeitsbereiche.
 *
 * @author Valentin Sackmann
 */
public enum TaetigkeitsbereichType {
    /**
     * _Physio_ mit dem internen Wert `P`.
     */
    PHYSIO("P"),
    /**
     * _Massage_ mit dem internen Wert `M`.
     */
    MASSAGE("M"),

    /**
     * _Diaet_ mit dem internen Wert `D`.
     */
    DIAET("D");

    private final String value;

    TaetigkeitsbereichType(final String value) {
        this.value = value;
    }

    /**
     * Konvertierung eines Strings in einen Enum-Wert.
     *
     * @param value Der String, zu dem ein passender Enum-Wert ermittelt werden soll.
     * @return Passender Enum-Wert oder null.
     */
    public static Optional<TaetigkeitsbereichType> of(final String value) {
        return Stream.of(values())
            .filter(taetigkeitsbereich -> Objects.equals(taetigkeitsbereich.value, value))
            .findFirst();
    }

    /**
     * Einen enum-Wert als String mit dem internen Wert ausgeben.
     * Dieser Wert wird durch Jackson in einem JSON-Datensatz verwendet.
     *
     * @return Interner Wert
     */
    @JsonValue
    @Override
    public String toString() {
        return value;
    }
}
