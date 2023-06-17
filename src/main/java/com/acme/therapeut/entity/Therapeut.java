package com.acme.therapeut.entity;

import jakarta.persistence.Column;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

/**
 * Daten eines Therapeuten.
 * <img src="../../../../../asciidoc/Therapeut.svg" alt="Klassendiagramm">
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
    @Transient
    @UniqueElements
    private List<TaetigkeitsbereichType> taetigkeitsbereiche;

    @Column(name = "taetigkeitsbereiche")
    private String taetigkeitsbereicheStr;

    @Size(max = 20)
    private String username;

    @CreationTimestamp
    private LocalDateTime erzeugt;

    @UpdateTimestamp
    private LocalDateTime aktualisiert;

    @PrePersist
    private void buildTaetigkeitsbereicheStr() {
        if (taetigkeitsbereiche == null || taetigkeitsbereiche.isEmpty()) {
            // NULL in der DB-Spalte
            taetigkeitsbereicheStr = null;
            return;
        }
        final var stringList = taetigkeitsbereiche.stream()
            .map(Enum::name)
            .toList();
        taetigkeitsbereicheStr = String.join(",", stringList);
    }

    @PostLoad
    private void loadInteressen() {
        if (taetigkeitsbereicheStr == null) {
            // NULL in der DB-Spalte
            taetigkeitsbereiche = emptyList();
            return;
        }
        final var interessenArray = taetigkeitsbereicheStr.split(",");
        taetigkeitsbereiche = Arrays.stream(interessenArray)
            .map(TaetigkeitsbereichType::valueOf)
            .collect(Collectors.toList());
    }
}
