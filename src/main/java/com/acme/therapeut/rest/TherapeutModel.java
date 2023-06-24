package com.acme.therapeut.rest;

import com.acme.therapeut.entity.Adresse;
import com.acme.therapeut.entity.GeschlechtType;
import com.acme.therapeut.entity.TaetigkeitsbereichType;
import com.acme.therapeut.entity.Therapeut;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Model-Klasse für Spring HATEOAS. @lombok.Data fasst die Annotationsn @ToString, @EqualsAndHashCode, @Getter, @Setter
 * und @RequiredArgsConstructor zusammen.
 *
 * @author Valentin Sackmann
 */
@JsonPropertyOrder({
    "nachname", "vorname", "email", "mitgliedId", "mitgliedNachname", "geburtsdatum", "geschlecht",
    "adresse", "taetigkeitsbereiche"
})
@Relation(collectionRelation = "therapeuten", itemRelation = "therapeut")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString(callSuper = true)
class TherapeutModel extends RepresentationModel<TherapeutModel> {
    private final String nachname;
    private final String vorname;
    private final UUID mitgliedId;
    private final String mitgliedNachname;
    private final String mitgliedVorname;
    @EqualsAndHashCode.Include
    private final String email;

    private final LocalDate geburtsdatum;
    private final GeschlechtType geschlecht;
    private final Adresse adresse;
    private final List<TaetigkeitsbereichType> taetigkeitsbereiche;

    TherapeutModel(final Therapeut therapeut) {
        mitgliedId = therapeut.getMitgliedId();
        mitgliedVorname = therapeut.getMitgliedVorname();
        mitgliedNachname = therapeut.getMitgliedNachname();
        nachname = therapeut.getNachname();
        vorname = therapeut.getVorname();
        email = therapeut.getEmail();
        geburtsdatum = therapeut.getGeburtsdatum();
        geschlecht = therapeut.getGeschlecht();
        adresse = therapeut.getAdresse();
        taetigkeitsbereiche = therapeut.getTaetigkeitsbereiche();
    }
}
