package com.acme.therapeut.repository;

import com.acme.therapeut.entity.TaetigkeitsbereichType;
import com.acme.therapeut.entity.Therapeut;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.IntStream;

import static com.acme.therapeut.repository.DB.THERAPEUTEN;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;

/**
 * Repository für den DB-Zugriff bei Therapeuten.
 *
 * @author Valentin Sackmann
 */
@Repository
@Slf4j
public class TherapeutRepository {
    /**
     * Einen Therapeuten anhand seiner ID suchen.
     *
     * @param id Die Id des gesuchten Therapeuten
     * @return Optional mit dem gefundenen Therapeuten oder leeres Optional
     */
    public Optional<Therapeut> findById(final UUID id) {
        return THERAPEUTEN.stream()
            .filter(kunde -> Objects.equals(kunde.getId(), id))
            .findFirst();
    }

    /**
     * Therpeuten anhand des Nachnamens suchen.
     *
     * @param nachname Der (Teil-) Nachname der gesuchten Therapeuten
     * @return Die gefundenen Therapeuten oder eine leere Collection
     */
    public @NonNull Collection<Therapeut> findByNachname(final CharSequence nachname) {
        log.debug("findByNachname: nachname={}", nachname);
        final var therapeuten = THERAPEUTEN.stream()
            .filter(therapeut -> therapeut.getNachname().contains(nachname))
            .toList();
        log.debug("findByNachname: therapeuten={}", therapeuten);
        return therapeuten;
    }

    /**
     * Therapeut zu gegebener Emailadresse aus der DB ermitteln.
     *
     * @param email Emailadresse für die Suche
     * @return Gefundener Therapeut oder leeres Optional
     */
    public Optional<Therapeut> findByEmail(final String email) {
        log.debug("findByEmail: {}", email);
        final var result = THERAPEUTEN.stream()
            .filter(therapeut -> Objects.equals(therapeut.getEmail(), email))
            .findFirst();
        log.debug("findByEmail: {}", result);
        return result;
    }

    /**
     * Therapeuten anhand von Suchkriterien ermitteln.
     *
     * @param suchkriterien Suchkriterien.
     * @return Gefundene Therapeuten oder leere Collection.
     */
    @SuppressWarnings({"ReturnCount", "JavadocLinkAsPlainText"})
    public @NonNull Collection<Therapeut> find(final Map<String, ? extends List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if (suchkriterien.isEmpty()) {
            return findAll();
        }

        for (final var entry : suchkriterien.entrySet()) {
            switch (entry.getKey()) {
                case "email" -> {
                    final var therapeutOpt = findByEmail(entry.getValue().get(0));
                    return therapeutOpt.isPresent() ? List.of(therapeutOpt.get()) : emptyList();
                }
                case "nachname" -> {
                    return findByNachname(entry.getValue().get(0));
                }
                case "taetigkeitsbereich" -> {
                    return findByTaetigkeitsbereich(entry.getValue());
                }
                default -> {
                    log.debug("find: ungueltiges Suchkriterium={}", entry.getKey());
                    return emptyList();
                }
            }
        }

        return emptyList();
    }

    /**
     * Therapeuten anhand von Taetigkeitsbereichen suchen.
     *
     * @param taetigkeitsbereicheStr Die Taetigkeitsbereiche der gesuchten Therapeuten
     * @return Die gefundenen Therapeuten oder eine leere Collection
     */
    private @NonNull Collection<Therapeut> findByTaetigkeitsbereich(final Collection<String> taetigkeitsbereicheStr) {
        log.debug("findByTaetigkeitsbereiche: taetigkeitsbereicheStr={}", taetigkeitsbereicheStr);
        final var taetigkeitsbereiche = taetigkeitsbereicheStr
            .stream()
            .map(taetigkeitsbereich -> TaetigkeitsbereichType.of(taetigkeitsbereich).orElse(null))
            .toList();
        if (taetigkeitsbereiche.contains(null)) {
            return emptyList();
        }
        final var therapeuten = THERAPEUTEN.stream()
            .filter(therapeut -> {
                @SuppressWarnings("SetReplaceableByEnumSet")
                final Collection<TaetigkeitsbereichType> therapeutTaetigkeitsbereiche = new HashSet<>(therapeut.getTaetigkeitsbereiche());
                return therapeutTaetigkeitsbereiche.containsAll(taetigkeitsbereiche);
            })
            .toList();
        log.debug("findByNachname: therapeuten={}", therapeuten);
        return therapeuten;
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

    /**
     * Einen vorhandenen Therapeuten aktualisieren.
     *
     * @param therapeut Das Objekt mit den neuen Daten
     */
    public void update(final @NonNull Therapeut therapeut) {
        log.debug("update: {}", therapeut);
        final OptionalInt index = IntStream
            .range(0, THERAPEUTEN.size())
            .filter(i -> Objects.equals(THERAPEUTEN.get(i).getId(), therapeut.getId()))
            .findFirst();
        log.trace("update: index={}", index);
        if (index.isEmpty()) {
            return;
        }
        THERAPEUTEN.set(index.getAsInt(), therapeut);
        log.debug("update: {}", therapeut);
    }
}
