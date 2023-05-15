package com.acme.therapeut.service;

import com.acme.therapeut.entity.Therapeut;
import com.acme.therapeut.repository.TherapeutRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Anwendungslogik für Therapeuten.
 * <img src="../../../../../asciidoc/TherapeutReadService.svg" alt="Klassendiagramm">
 *
 * @author Valentin Sackmann
 */
@Service
@Slf4j
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

    /**
     * Therapeuten anhand von Suchkriterien als Collection suchen.
     *
     * @param suchkriterien Die Suchkriterien
     * @return Die gefundenen Therapeuten oder eine leere Liste
     * @throws NotFoundException Falls keine Therapeuten gefunden wurden
     */
    @SuppressWarnings({"ReturnCount", "NestedIfDepth"})
    public @NonNull Collection<Therapeut> find(@NonNull final Map<String, List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return repo.findAll();
        }

        if (suchkriterien.size() == 1) {
            final var nachnamen = suchkriterien.get("nachname");
            if (nachnamen != null && nachnamen.size() == 1) {
                final var therapeuten = repo.findByNachname(nachnamen.get(0));
                if (therapeuten.isEmpty()) {
                    throw new NotFoundException(suchkriterien);
                }
                log.debug("find (nachname): {}", therapeuten);
                return therapeuten;
            }

            final var emails = suchkriterien.get("email");
            if (emails != null && emails.size() == 1) {
                final var therapeut = repo.findByEmail(emails.get(0));
                if (therapeut.isEmpty()) {
                    throw new NotFoundException(suchkriterien);
                }
                final var therapeuten = List.of(therapeut.get());
                log.debug("find (email): {}", therapeuten);
                return therapeuten;
            }
        }

        final var therapeuten = repo.find(suchkriterien);
        if (therapeuten.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        log.debug("find: {}", therapeuten);
        return therapeuten;
    }



}
