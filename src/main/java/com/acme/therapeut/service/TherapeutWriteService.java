package com.acme.therapeut.service;

import com.acme.therapeut.entity.Therapeut;
import com.acme.therapeut.repository.TherapeutRepository;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * Anwendungslogik für Therapeuten.
 * <img src="../../../../../asciidoc/TherapeutWriteService.svg" alt="Klassendiagramm">
 *
 * @author Valentin Sackmann
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TherapeutWriteService {
    private final TherapeutRepository repo;

    private final Validator validator;

    /**
     * Einen neuen Therapeuten anlegen.
     *
     * @param therapeut Das Objekt des neu anzulegenden Therapeuten.
     * @return Der neu angelegte Therapeut mit generierter ID
     * @throws EmailExistsException Es gibt bereits einen Therapeutn mit der Emailadresse.
     */
    @Transactional
    public Therapeut create(final Therapeut therapeut) {
        log.debug("create: {}", therapeut);

        final var violations = validator.validate(therapeut);
        if (!violations.isEmpty()) {
            log.debug("create: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }

        if (repo.existsByEmail(therapeut.getEmail())) {
            throw new EmailExistsException(therapeut.getEmail());
        }

        final var therapeutDB = repo.save(therapeut);
        log.debug("create: {}", therapeutDB);
        return therapeutDB;
    }

    /**
     * Einen vorhandenen Therapeuten aktualisieren.
     *
     * @param therapeut Das Objekt mit den neuen Daten (ohne ID)
     * @param id ID des zu aktualisierenden Therapeuten
     * @param version Die erforderliche Version
     * @return Aktualisierter Therapeut mit erhöhter Versionsnummer
     * @throws ConstraintViolationsException Falls mindestens ein Constraint verletzt ist.
     * @throws NotFoundException Kein Therapeut zur ID vorhanden.
     * @throws EmailExistsException Es gibt bereits einen Therapeuten mit der Emailadresse.
     */
    public Therapeut update(final Therapeut therapeut, final UUID id, final int version) {
        log.debug("update: {}", therapeut);
        log.debug("update: id={}", id);

        final var violations = validator.validate(therapeut);
        if (!violations.isEmpty()) {
            log.debug("update: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }

        final var therapeutDbOptional = repo.findById(id);
        if (therapeutDbOptional.isEmpty()) {
            throw new NotFoundException(id);
        }

        final var email = therapeut.getEmail();
        final var therapeutDB = therapeutDbOptional.get();

        log.trace("update: version={}, therapeutDb={}", version, therapeutDB);
        if (version != therapeutDB.getVersion()) {
            throw new VersionOutdatedException(version);
        }
        // Ist die neue Email bei einem *ANDEREN* Therapeuten vorhanden?
        if (!Objects.equals(email, therapeutDB.getEmail()) && repo.existsByEmail(email)) {
            log.debug("update: email {} existiert", email);
            throw new EmailExistsException(email);
        }

        therapeutDB.set(therapeut);
        repo.save(therapeut);
        return therapeutDB;
    }
}
