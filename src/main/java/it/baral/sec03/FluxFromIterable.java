package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Classe dimostrativa della sezione 3 del corso: mostra la creazione di un
 * {@link Flux} a partire da una collezione esistente tramite
 * {@link Flux#fromIterable(Iterable)} e da un array tramite
 * {@link Flux#fromArray(Object[])}.
 */
public class FluxFromIterable {

	/**
	 * Sottoscrive due {@link Flux}, uno creato da una {@link List} e uno da
	 * un array.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		List<String> list = List.of("A", "B", "C", "D", "E");
		Flux.fromIterable(list)
			.subscribe(Util.subscriber());
		
		Integer[] array = {1, 2, 3, 4, 5};
		Flux.fromArray(array)
			.subscribe(Util.subscriber());
	}
}
