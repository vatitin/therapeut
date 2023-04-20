package com.acme.therapeut.repository;

import com.acme.therapeut.entity.Therapeut;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.acme.therapeut.repository.DB.THERAPEUTEN;

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

}
