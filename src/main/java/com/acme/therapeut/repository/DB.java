/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.therapeut.repository;

/**
 * Emulation der Datenbasis für persistente Therapeuten.
 */
@SuppressWarnings({"UtilityClassCanBeEnum", "UtilityClass", "MagicNumber", "RedundantSuppression"})
final class DB {
    /**
     * Liste der Therapeuten zur Emulation der DB.
     */
    /*
    @SuppressWarnings("StaticCollection")
    static final List<Therapeut> THERAPEUTEN = getTherapeuten();

    private DB() {
    }

    @SuppressWarnings({"FeatureEnvy", "TrailingComment"})
    private static List<Therapeut> getTherapeuten() {

        final var therapeuten = Stream.of(
            // admin
            Therapeut.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                .nachname("admin")
                .vorname("Admin-Vorname")
                .email("admin@acme.com")
                .geburtsdatum(LocalDate.parse("2002-03-12"))
                .geschlecht(WEIBLICH)
                .taetigkeitsbereiche(List.of(PHYSIO))
                .adresse(Adresse.builder().plz("77933").ort("Lahr").build())
                .build(),
            // HTTP GET
            Therapeut.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .nachname("Mustermann")
                .vorname("Max")
                .email("max-mustermann@acme.com")
                .geburtsdatum(LocalDate.parse("2002-01-12"))
                .geschlecht(MAENNLICH)
                .taetigkeitsbereiche(List.of(PHYSIO, MASSAGE))
                .adresse(Adresse.builder().plz("76137").ort("Karlsruhe").build())
                .build(),
            Therapeut.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000020"))
                .nachname("Musterfrau")
                .vorname("Maja")
                .email("maja-musterfrau@acme.com")
                .geburtsdatum(LocalDate.parse("2002-08-04"))
                .geschlecht(WEIBLICH)
                .taetigkeitsbereiche(List.of(MASSAGE))
                .adresse(Adresse.builder().plz("80331").ort("München").build())
                .build(),

            // zur freien Verfuegung
            Therapeut.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000060"))
                .nachname("Müller")
                .vorname("Anton")
                .email("Anton-Müller@acme.com")
                .geburtsdatum(LocalDate.parse("1998-08-04"))
                .geschlecht(DIVERS)
                .taetigkeitsbereiche(List.of(MASSAGE))
                .adresse(Adresse.builder().plz("50667").ort("Köln").build())
                .build())
            .collect(Collectors.toList());

        // Rueckwaertsverweise fuer Adresse
        therapeuten.forEach(therapeut -> therapeut.getAdresse().setTherapeut(therapeut));

        return therapeuten;
    }

     */

}
