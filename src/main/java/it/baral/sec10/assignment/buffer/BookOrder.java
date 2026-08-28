package it.baral.sec10.assignment.buffer;

import com.github.javafaker.Book;
import it.baral.common.Util;

/**
 * Rappresenta un ordine di un libro utilizzato nell'esercizio sull'operatore {@code buffer}
 * (sezione sec10).
 *
 * @param genre genere del libro ordinato
 * @param title titolo del libro ordinato
 * @param price prezzo dell'ordine
 */
public record BookOrder(String genre, String title, Integer price) {

	/**
	 * Crea un ordine di libro casuale generato tramite la libreria Faker.
	 *
	 * @return un nuovo {@code BookOrder} con genere, titolo e prezzo generati casualmente
	 */
	public static BookOrder create() {
		Book book = Util.faker().book();
		return new BookOrder(book.genre(), book.title(), Util.faker().random().nextInt(10, 100));
	}
}
