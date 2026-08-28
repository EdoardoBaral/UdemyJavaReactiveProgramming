package it.baral.sec10.assignment.groupby;

import com.github.javafaker.Commerce;
import it.baral.common.Util;

/**
 * Rappresenta un ordine di acquisto utilizzato nell'esercizio sull'operatore {@code groupBy}
 * (sezione sec10).
 *
 * @param item nome del prodotto acquistato
 * @param category categoria del prodotto acquistato
 * @param price prezzo dell'ordine
 */
public record PurchaseOrder(String item, String category, Integer price) {

	/**
	 * Crea un ordine di acquisto casuale generato tramite la libreria Faker.
	 *
	 * @return un nuovo {@code PurchaseOrder} con prodotto, categoria e prezzo generati casualmente
	 */
	public static PurchaseOrder create() {
		Commerce commerce = Util.faker().commerce();
		return new PurchaseOrder(commerce.productName(), commerce.department(), Util.faker().random().nextInt(10, 100));
	}
}
