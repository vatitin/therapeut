package com.acme.therapeut.repository;

import com.acme.therapeut.entity.Therapeut;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.acme.therapeut.repository.DB.THERAPEUTEN;
import static java.util.UUID.randomUUID;

/**
 * Repository für den DB-Zugriff bei Therapeuten.
 *
 * @author Valentin Sackmann
 */
@Repository
public class TherapeutRepository {
    /**
     * Einen Therapeuten anhand seiner ID suchen.
     *
     * @param id Die Id des gesuchten Therapeuten
     * @return Optional mit dem gefundenen Therapeuten oder leeres Optional
     */
    public Optional<Therapeut> findById(final UUID id) {
        final var result = THERAPEUTEN.stream()
            .filter(kunde -> Objects.equals(kunde.getId(), id))
            .findFirst();
        return result;
    }

    /**
     * Alle Therapeuten als Collection ermitteln.
     *
     * @return Alle Therapeuten
     */
    public @NonNull Collection<Therapeut> findAll() {
        return THERAPEUTEN;
    }

    /**
     * Abfrage, ob es einen Therapeuten mit gegebener Emailadresse gibt.
     *
     * @param email Emailadresse für die Suche
     * @return true, falls es einen solchen Therapeuten gibt, sonst false
     */
    public boolean isEmailExisting(final String email) {
        final var count = THERAPEUTEN.stream()
            .filter(therapeut -> Objects.equals(therapeut.getEmail(), email))
            .count();
        return count > 0L;
    }

    /**
     * Einen neuen Therapeuten anlegen.
     *
     * @param therapeut Das Objekt des neu anzulegenden Therapeuten.
     * @return Der neu angelegte Therapeut mit generierter ID
     */
    public @NonNull Therapeut create(final @NonNull Therapeut therapeut) {
        therapeut.setId(randomUUID());
        THERAPEUTEN.add(therapeut);
        return therapeut;
    }
}
