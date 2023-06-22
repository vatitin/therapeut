package com.acme.therapeut.repository;

import com.acme.therapeut.entity.Therapeut;
import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.acme.therapeut.entity.Therapeut.ADRESSE_GRAPH;

/**
 * Repository für den DB-Zugriff bei Therapeuten.
 *
 * @author Valentin Sackmann
 */
@Repository
public interface TherapeutRepository extends JpaRepository<Therapeut, UUID>, QuerydslPredicateExecutor<Therapeut> {
    @EntityGraph(ADRESSE_GRAPH)
    @Override
    List<Therapeut> findAll();

    @EntityGraph(ADRESSE_GRAPH)
    // @EntityGraph(ADRESSE_UMSAETZE_GRAPH) // NOSONAR
    @Override
    List<Therapeut> findAll(Predicate predicate);

    @EntityGraph(ADRESSE_GRAPH)
    // @EntityGraph(ADRESSE_UMSAETZE_GRAPH) // NOSONAR
    @Override
    Optional<Therapeut> findById(UUID id);


    /**
     * Therapeut zu gegebener Emailadresse aus der DB ermitteln.
     *
     * @param email Emailadresse für die Suche
     * @return Optional mit dem gefundenen Therapeuten oder leeres Optional
     */
    @Query("""
        SELECT t
        FROM   Therapeut t
        WHERE  lower(t.email) LIKE concat(lower(:email), '%')
        """)
    @EntityGraph(ADRESSE_GRAPH)
    Optional<Therapeut> findByEmail(String email);

    /**
     * Therapeuten anhand des Nachnamens suchen.
     *
     * @param nachname Der (Teil-) Nachname der gesuchten Therapeuten
     * @return Die gefundenen Therapeuten oder eine leere Collection
     */
    @Query("""
        SELECT   t
        FROM     Therapeut t
        WHERE    lower(t.nachname) LIKE concat('%', lower(:nachname), '%')
        ORDER BY t.id
        """)
    @EntityGraph(ADRESSE_GRAPH)
    Collection<Therapeut> findByNachname(CharSequence nachname);

    /**
     * Abfrage, ob es einen Therapeuten mit gegebener Emailadresse gibt.
     *
     * @param email Emailadresse für die Suche
     * @return true, falls es einen solchen Therapeuten gibt, sonst false
     */
    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    boolean existsByEmail(String email);

    /**
     * Therapeut zu gegebener Mitglied-ID aus der DB ermitteln.
     *
     * @param mitgliedId Kunde-ID für die Suche
     * @return Liste der gefundenen Therapeuten
     */
    @EntityGraph(ADRESSE_GRAPH)
    List<Therapeut> findByMitgliedId(UUID mitgliedId);
}
