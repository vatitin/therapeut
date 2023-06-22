package com.acme.therapeut.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

import static com.acme.therapeut.entity.Therapeut.ADRESSE_GRAPH;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static java.util.Collections.emptyList;

/**
 * Daten eines Therapeuten.
 * <img src="../../../../../asciidoc/Therapeut.svg" alt="Klassendiagramm">
 *
 * @author Valentin Sackmann
 */
@Entity
@Table(name = "therapeut")
@NamedEntityGraph(name = ADRESSE_GRAPH, attributeNodes = @NamedAttributeNode("adresse"))
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString
@Builder
@SuppressWarnings({
    "ClassFanOutComplexity",
    "RequireEmptyLineBeforeBlockTagGroup",
    "DeclarationOrder",
    "MagicNumber",
    "JavadocDeclaration",
    "MissingSummary",
    "RedundantSuppression"})
public class Therapeut {

    /**
     * NamedEntityGraph für das Attribut "adresse".
     */
    public static final String ADRESSE_GRAPH = "Therapeut.adresse";

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
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Der Nachname des Therapeuten.
     */
    @NotNull
    @Pattern(regexp = NACHNAME_PATTERN)
    @Size(max = 40)
    private String nachname;

    /**
     * Der Vorname des Therapeuten.
     */
    @Size(max = 40)
    @NotNull
    @Pattern(regexp = VORNAME_PATTERN)
    private String vorname;

    /**
     * Die Emailadresse des Therapeuten.
     */
    @Email
    @NotNull
    @Size(max = 20)
    private String email;

    /**
     * Das Geburtsdatum des Therapeuten.
     */
    @Past
    private LocalDate geburtsdatum;

    /**
     * Das Geschlecht des Therapeuten.
     */
    @Enumerated(STRING)
    private GeschlechtType geschlecht;

    /**
     * Die Adresse des Therapeuten.
     */
    @OneToOne(mappedBy = "therapeut", optional = false, cascade = {PERSIST, REMOVE}, fetch = LAZY, orphanRemoval = true)
    @Valid
    @ToString.Exclude
    private Adresse adresse;

    @Column(name = "mitglied")
    private UUID mitgliedId;

    @Version
    public int version;

    /**
     * Die Taetigkeitsbereiche des Therapeuten.
     */
    @Transient
    @UniqueElements
    private List<TaetigkeitsbereichType> taetigkeitsbereiche;

    @Column(name = "taetigkeitsbereiche")
    private String taetigkeitsbereicheStr;

    @Transient
    private String mitgliedNachname;

    @Transient
    private String mitgliedVorname;

    @CreationTimestamp
    private LocalDateTime erzeugt;

    @UpdateTimestamp
    private LocalDateTime aktualisiert;

    /**
     * Therapeutendaten überschreiben.
     *
     * @param therapeut Neue Therapeutendaten.
     */
    public void set(final Therapeut therapeut) {
        nachname = therapeut.nachname;
        vorname = therapeut.vorname;
        email = therapeut.email;
        geburtsdatum = therapeut.geburtsdatum;
        geschlecht = therapeut.geschlecht;
    }

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
    private void loadTaetigkeitsbereiche() {
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
