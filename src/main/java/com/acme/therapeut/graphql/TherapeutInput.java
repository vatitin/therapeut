package com.acme.therapeut.graphql;

import com.acme.therapeut.entity.Adresse;
import com.acme.therapeut.entity.GeschlechtType;
import com.acme.therapeut.entity.TaetigkeitsbereichType;
import com.acme.therapeut.entity.Therapeut;

import java.time.LocalDate;
import java.util.List;

/**
 * Eine Value-Klasse für Eingabedaten passend zu TherapeutInput aus dem GraphQL-Schema.
 *
 * @author Valentin Sackmann
 * @param nachname Nachname
 * @param vorname Vorname
 * @param email Emailadresse
 * @param geburtsdatum Geburtsdatum
 * @param geschlecht Geschlecht
 * @param adresse Adresse
 * @param taetigkeitsbereiche Taetigkeitsbereiche als Liste
 */
@SuppressWarnings("RecordComponentNumber")
record TherapeutInput(
    String nachname,
    String vorname,
    String email,
    String geburtsdatum,
    GeschlechtType geschlecht,
    AdresseInput adresse,
    List<TaetigkeitsbereichType> taetigkeitsbereiche
) {
    /**
     * Konvertierung in ein Objekt der Entity-Klasse Therapeut.
     *
     * @return Das konvertierte Therapeut-Objekt
     */
    Therapeut toTherapeut() {
        final var geburtsdatumTmp = LocalDate.parse(geburtsdatum);
        final var adresseEntity = Adresse.builder().plz(adresse.plz()).ort(adresse.ort()).build();

        final var therapeut = Therapeut
            .builder()
            .id(null)
            .nachname(nachname)
            .vorname(vorname)
            .email(email)
            .geburtsdatum(geburtsdatumTmp)
            .geschlecht(geschlecht)
            .taetigkeitsbereiche(taetigkeitsbereiche)
            .adresse(adresseEntity)
            .build();

        // Rueckwaertsverweise
        therapeut.getAdresse()
            .setTherapeut(therapeut);

        return therapeut;
    }
}
