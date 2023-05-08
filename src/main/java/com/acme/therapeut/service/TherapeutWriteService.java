package com.acme.therapeut.service;

import com.acme.therapeut.entity.Therapeut;
import com.acme.therapeut.repository.TherapeutRepository;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * Anwendungslogik für Therapeuten.
 *
 * @author Valentin Sackmann
 */
@Service
@RequiredArgsConstructor
@Slf4j
public final class TherapeutWriteService {
    private final TherapeutRepository repo;

    private final Validator validator;

    /**
     * Einen neuen Therapeuten anlegen.
     *
     * @param therapeut Das Objekt des neu anzulegenden Therapeuten.
     * @return Der neu angelegte Therapeut mit generierter ID
     * @throws EmailExistsException Es gibt bereits einen Therapeutn mit der Emailadresse.
     */
    public Therapeut create(final Therapeut therapeut) {
        log.debug("create: {}", therapeut);

        final var violations = validator.validate(therapeut);
        if (!violations.isEmpty()) {
            log.debug("create: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }

        if (repo.isEmailExisting(therapeut.getEmail())) {
            throw new EmailExistsException(therapeut.getEmail());
        }

        final var therapeutDB = repo.create(therapeut);
        log.debug("create: {}", therapeutDB);
        return therapeutDB;
    }

    /**
     * Einen vorhandenen Therapeuten aktualisieren.
     *
     * @param therapeut Das Objekt mit den neuen Daten (ohne ID)
     * @param id ID des zu aktualisierenden Therapeuten
     * @throws ConstraintViolationsException Falls mindestens ein Constraint verletzt ist.
     * @throws NotFoundException Kein Therapeut zur ID vorhanden.
     * @throws EmailExistsException Es gibt bereits einen Therapeuten mit der Emailadresse.
     */
    public void update(final Therapeut therapeut, final UUID id) {
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
        // Ist die neue Email bei einem *ANDEREN* Therapeuten vorhanden?
        if (!Objects.equals(email, therapeutDB.getEmail()) && repo.isEmailExisting(email)) {
            log.debug("update: email {} existiert", email);
            throw new EmailExistsException(email);
        }

        therapeut.setId(id);
        repo.update(therapeut);
    }
}
