package com.acme.therapeut.rest;

import com.acme.therapeut.entity.Adresse;
import com.acme.therapeut.entity.GeschlechtType;
import com.acme.therapeut.entity.TaetigkeitsbereichType;
import com.acme.therapeut.entity.Therapeut;

import java.time.LocalDate;
import java.util.List;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Therapeuten.
 *      Beim Lesen wird die Klasse Therapeut für die Ausgabe
 *      verwendet.
 *
 * @author Valentin Sackmann
 * @param nachname Gültiger Nachname eines Therapeuten, d.h. mit einem geeigneten Muster.
 * @param vorname Gültiger Vorname eines Therapeuten.
 * @param email Email eines Therapeuten.
 * @param geburtsdatum Das Geburtsdatum eines Therapeuten.
 * @param geschlecht Das Geschlecht eines Therapeuten.
 * @param adresse Die Adresse eines Therapeuten.
 * @param taetigkeitsbereiche Die Taetigkeitsbereiche eines Therapeuten.
 */
@SuppressWarnings("RecordComponentNumber")
record TherapeutDTO(
    String nachname,
    String vorname,
    String email,
    LocalDate geburtsdatum,
    GeschlechtType geschlecht,
    AdresseDTO adresse,
    List<TaetigkeitsbereichType> taetigkeitsbereiche
) {
    /**
     * Konvertierung in ein Objekt des Anwendungskerns.
     *
     * @return Therapeutobjekt für den Anwendungskern
     */
    Therapeut toTherapeut() {
        final var adresseEntity = adresse() == null
            ? null
            : Adresse
            .builder()
            .plz(adresse().plz())
            .ort(adresse().ort())
            .build();

        final var therapeut = Therapeut
            .builder()
            .id(null)
            .nachname(nachname)
            .vorname(vorname)
            .email(email)
            .geburtsdatum(geburtsdatum)
            .geschlecht(geschlecht)
            .adresse(adresseEntity)
            .taetigkeitsbereiche(taetigkeitsbereiche)
            .build();

        // Rueckwaertsverweise
        therapeut.getAdresse()
            .setTherapeut(therapeut);

        return therapeut;
    }
}
