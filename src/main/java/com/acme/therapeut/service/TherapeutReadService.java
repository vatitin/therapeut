package com.acme.therapeut.service;

import com.acme.therapeut.entity.Therapeut;
import com.acme.therapeut.repository.TherapeutRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

/**
 * Anwendungslogik für Therapeuten.
 *
 * @author Valentin Sackmann
 */
@Service
@RequiredArgsConstructor
public final class TherapeutReadService {

    private final TherapeutRepository repo;

    /**
     * Alle Therapeuten suchen.
     *
     * @return Die gefundenen Therapeuten oder leere Collection.
     */
    public @NonNull Collection<Therapeut> findAll() {
        return repo.findAll();
    }

    /**
     * Einen Therapeuten anhand seiner ID suchen.
     *
     * @param id Die Id des gesuchten Therapeuten
     * @return Der gefundene Therapeut
     * @throws NotFoundException Falls kein Therapeut gefunden wurde
     */
    public @NonNull Therapeut findById(final UUID id) {
        return repo.findById(id)
            .orElseThrow(() -> new NotFoundException(id));
    }



}
