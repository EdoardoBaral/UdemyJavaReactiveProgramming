package it.baral.sec02;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Classe dimostrativa della sezione 2 del corso: mostra {@link Mono#fromSupplier(java.util.function.Supplier)},
 * che esegue in modo lazy un {@link java.util.function.Supplier} solo al
 * momento della sottoscrizione, producendone il risultato come valore del
 * {@link Mono}.
 */
public class MonoFromSupplier {

	private static final Logger logger = LoggerFactory.getLogger(MonoFromSupplier.class);

	/**
	 * Crea un {@link Mono} tramite {@code fromSupplier} che calcola la somma
	 * di una lista di interi e vi si sottoscrive.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		List<Integer> list = List.of(1, 2, 3, 4, 5);
		Mono.fromSupplier(() -> sum(list))
			.subscribe(Util.subscriber());
	}

	/**
	 * Calcola la somma degli elementi della lista fornita.
	 *
	 * @param list la lista di interi di cui calcolare la somma
	 * @return la somma degli elementi della lista
	 */
	private static int sum(List<Integer> list) {
		logger.info("Calculating sum of {}", list);
		return list.stream()
				   .mapToInt(Integer::intValue)
				   .sum();
	}
}
