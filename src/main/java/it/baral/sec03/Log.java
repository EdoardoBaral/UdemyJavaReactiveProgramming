package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

/**
 * Classe dimostrativa della sezione 3 del corso: mostra l'operatore
 * {@code log()}, che registra automaticamente tutti i segnali Reactive
 * Streams (sottoscrizione, richiesta, elementi, completamento) attraversati
 * dalla catena, utile a scopo di debug.
 */
public class Log {

	/**
	 * Sottoscrive un {@link Flux} con due punti di logging distinti (uno
	 * prima e uno dopo l'operatore {@code map}, quest'ultimo identificato da
	 * una categoria specifica).
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux.range(1, 10)
			.log()
			.map(x -> Util.faker().name().name())
			.log("map-log")
			.subscribe(Util.subscriber("Sub1"));
	}
}
