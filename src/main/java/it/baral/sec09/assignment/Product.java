package it.baral.sec09.assignment;

/**
 * Rappresenta un prodotto arricchito con recensione e prezzo, risultato tipico
 * dell'esercizio della sezione 09 che combina pi&ugrave; sorgenti dati con {@code zip}.
 *
 * @param name   il nome del prodotto
 * @param review la recensione associata al prodotto
 * @param price  il prezzo del prodotto
 */
public record Product(String name, String review, String price) {
}
