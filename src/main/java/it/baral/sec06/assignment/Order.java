package it.baral.sec06.assignment;

/**
 * Rappresenta un ordine di acquisto ricevuto dallo stream esterno, con la categoria
 * del prodotto, il prezzo e la quantità acquistata.
 *
 * @param category categoria del prodotto ordinato
 * @param price prezzo dell'ordine
 * @param quantity quantità acquistata
 */
public record Order(String category, Integer price, Integer quantity) {
}
