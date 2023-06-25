package com.acme.therapeut.repository;

/**
 * Entity-Klasse für den REST-Client.
 *
 * @author Valentin Sackmann
 * @param name Nachmane
 * @param vorname Vorname
 */
public record Mitglied(String name, String vorname) {
}
