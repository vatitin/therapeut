package com.acme.therapeut.graphql;

/**
 * Adressdaten.
 *
 * @author Valentin Sackmann
 *
 * @param plz Die 5-stellige Postleitzahl als unveränderliches Pflichtfeld.
 * @param ort Der Ort als unveränderliches Pflichtfeld.
 */
record AdresseInput(String plz, String ort) {
}
