package com.acme.therapeut.graphql;

import java.util.UUID;

/**
 * Value-Klasse für das Resultat, wenn an der GraphQL-Schnittstelle ein neuer Therapeut angelegt wurde.
 *
 * @author Valentin Sackmann
 *
 * @param id ID des neu angelegten Therapeuten
 */
record CreatePayload(UUID id) {
}
