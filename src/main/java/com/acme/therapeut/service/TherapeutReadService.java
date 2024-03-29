package com.acme.therapeut.service;

import com.acme.therapeut.entity.Therapeut;
import com.acme.therapeut.repository.Mitglied;
import com.acme.therapeut.repository.MitgliedRestRepository;
import com.acme.therapeut.repository.PredicateBuilder;
import com.acme.therapeut.repository.TherapeutRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_MODIFIED;

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
    private final PredicateBuilder predicateBuilder;
    private final MitgliedRestRepository mitgliedRepository;

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

        final var predicate = predicateBuilder
            .build(suchkriterien)
            .orElseThrow(() -> new NotFoundException(suchkriterien));
        final var therapeuten = repo.findAll(predicate);
        if (therapeuten.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        log.debug("find: {}", therapeuten);
        return therapeuten;
    }

    /**
     * Therapeuten zur Mitglied-ID suchen.
     *
     * @param mitgliedId Die Id des gegebenen Mitglieds.
     * @return Die gefundenen Therapeuten.
     * @throws NotFoundException Falls keine Therapeuten gefunden wurden.
     */
    public Collection<Therapeut> findByMitgliedId(final UUID mitgliedId) {
        log.debug("findByMitgliedId: mitgliedId={}", mitgliedId);

        final var therapeuten = repo.findByMitgliedId(mitgliedId);
        if (therapeuten.isEmpty()) {
            throw new NotFoundException();
        }

        final var mitglied = findMitgliedById(mitgliedId);
        final var name = mitglied == null ? null : mitglied.name();
        final var vorname = mitglied == null ? null : mitglied.vorname();
        log.trace("findByMitgliedId: nachname={}, email={}", name, vorname);
        therapeuten.forEach(therapeut -> {
            therapeut.setMitgliedName(name);
            therapeut.setMitgliedVorname(vorname);
        });

        log.trace("findByMitgliedId: therapeuten={}", therapeuten);
        return therapeuten;
    }

    @SuppressWarnings("ReturnCount")
    private Mitglied findMitgliedById(final UUID mitgliedId) {
        log.debug("findMitgliedById: mitgliedId={}", mitgliedId);

        final ResponseEntity<Mitglied> response;
        try {
            response = mitgliedRepository.getMitglied(mitgliedId.toString());
        } catch (final WebClientResponseException.NotFound ex) {
            // Statuscode 404
            log.error("findMitgliedById: WebClientResponseException.NotFound");
            return new Mitglied("N/A", "not.found@acme.com");
        } catch (final WebClientException ex) {
            // sonstiger Statuscode 4xx oder 5xx
            // WebClientRequestException oder WebClientResponseException (z.B. ServiceUnavailable)
            log.error("findMitgliedById: {}", ex.getClass().getSimpleName());
            return new Mitglied("Exception", "exception@acme.com");
        }

        final var statusCode = response.getStatusCode();
        log.debug("findMitgliedById: statusCode={}", statusCode);
        if (statusCode == NOT_MODIFIED) {
            return new Mitglied("Not-Modified", "not.modified@acme.com");
        }

        final var mitglied = response.getBody();
        log.debug("findMitgliedById: {}", mitglied);
        return mitglied;
    }



}
