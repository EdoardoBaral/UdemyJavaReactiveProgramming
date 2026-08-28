package it.baral.sec09.applications;

/**
 * Rappresenta un ordine effettuato da un utente nella mini-applicazione di esempio
 * della sezione sec09.
 *
 * @param userId identificativo dell'utente che ha effettuato l'ordine
 * @param productName nome del prodotto ordinato
 * @param price prezzo dell'ordine
 */
public record Order(Integer userId, String productName, Integer price) {
}
