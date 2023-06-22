package com.acme.therapeut.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

/**
 * Adressdaten.
 *
 *  @author Valentin Sackmann
 */
@Entity
@Table(name = "adresse")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@SuppressWarnings({"JavadocDeclaration", "RequireEmptyLineBeforeBlockTagGroup"})
public class Adresse {
    /**
     * Konstante für den regulären Ausdruck einer Postleitzahl als 5-stellige Zahl mit führender Null.
     */
    public static final String PLZ_PATTERN = "^\\d{5}$";

    @Id
    @GeneratedValue
    @JsonIgnore
    private UUID id;

    /**
     * Die Postleitzahl für die Adresse.
     * @param plz Die Postleitzahl als String
     * @return Die Postleitzahl als String
     */
    @NotNull
    @Pattern(regexp = PLZ_PATTERN)
    private String plz;

    /**
     * Der Ort für die Adresse.
     * @param ort Der Ort als String
     * @return Der Ort als String
     */
    @NotBlank
    private String ort;

    /**
     * Der zugehörige Therapeut.
     * @param therapeut Der Therapeut.
     * @return Der Therapeut.
     */
    @OneToOne(optional = false, fetch = LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Therapeut therapeut;
}
