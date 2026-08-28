package it.baral.sec09.helper;

/**
 * Rappresenta un'offerta di volo restituita da una compagnia aerea, usata negli
 * esempi di {@code merge} della sezione 09.
 *
 * @param airline il nome della compagnia aerea
 * @param price   il prezzo dell'offerta
 */
public record Flight(String airline, Integer price) {
}
