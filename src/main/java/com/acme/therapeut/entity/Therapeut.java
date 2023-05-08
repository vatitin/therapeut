package com.acme.therapeut.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Daten eines Therapeuten.
 *
 * @author Valentin Sackmann
 */
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString
public class Therapeut {

    /**
     * Muster für einen gültigen Nachnamen.
     */
    public static final String NACHNAME_PATTERN =
        "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    /**
     * Muster für einen gültigen Vornamen.
     */
    public static final String VORNAME_PATTERN =
        "[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    /**
     * Die ID des Therapeuten.
     */
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Der Nachname des Therapeuten.
     */
    @NotNull
    @Pattern(regexp = NACHNAME_PATTERN)
    private String nachname;

    /**
     * Der Vorname des Therapeuten.
     */
    @NotNull
    @Pattern(regexp = VORNAME_PATTERN)
    private String vorname;

    /**
     * Die Emailadresse des Therapeuten.
     */
    @Email
    @NotNull
    private String email;

    /**
     * Das Geburtsdatum des Therapeuten.
     */
    @Past
    private LocalDate geburtsdatum;


    /**
     * Das Geschlecht des Therapeuten.
     */
    private GeschlechtType geschlecht;

    /**
     * Die Adresse des Therapeuten.
     */
    @Valid
    @ToString.Exclude
    private Adresse adresse;


    /**
     * Die Taetigkeitsbereiche des Therapeuten.
     */
    @UniqueElements
    @ToString.Exclude
    private List<TaetigkeitsbereichType> taetigkeitsbereiche;


}
