package com.acme.therapeut.service;


import com.acme.therapeut.entity.Therapeut;
import com.acme.therapeut.repository.TherapeutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    /**
     * Einen neuen Therapeuten anlegen.
     *
     * @param therapeut Das Objekt des neu anzulegenden Therapeuten.
     * @return Der neu angelegte Therapeut mit generierter ID
     * @throws EmailExistsException Es gibt bereits einen Therapeutn mit der Emailadresse.
     */
    public Therapeut create(final Therapeut therapeut) {
        log.debug("create: {}", therapeut);

        if (repo.isEmailExisting(therapeut.getEmail())) {
            throw new EmailExistsException(therapeut.getEmail());
        }

        final var therapeutDB = repo.create(therapeut);
        log.debug("create: {}", therapeutDB);
        return therapeutDB;
    }
}
