package it.baral.sec02;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Classe dimostrativa della sezione 2 del corso: mostra {@link Mono#defer(java.util.function.Supplier)},
 * che rimanda la creazione del {@link Mono} effettivo al momento della
 * sottoscrizione, cosi' che il {@link java.util.function.Supplier} fornito
 * (e quindi la sua eventuale logica costosa) venga eseguito una volta per
 * ogni sottoscrizione, invece che una sola volta alla creazione.
 */
public class MonoDefer {

	private static final Logger log = LoggerFactory.getLogger(MonoDefer.class);

	/**
	 * Sottoscrive un {@link Mono} creato tramite {@code defer}, che a sua
	 * volta costruisce il publisher effettivo invocando {@link #createPublisher()}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Mono.defer(MonoDefer::createPublisher)
			.subscribe(Util.subscriber());
	}

	/**
	 * Simula la creazione (costosa) del publisher effettivo, restituendo un
	 * {@link Mono} che calcolera' la somma di una lista di interi.
	 *
	 * @return un {@link Mono} che calcolera' la somma della lista al momento della sottoscrizione
	 */
	private static Mono<Integer> createPublisher() {
		log.info("Creating publisher");
		List<Integer> list = List.of(1, 2, 3, 4, 5);
		Util.sleepSeconds(3);

		return Mono.fromSupplier(() -> sum(list));
	}

	/**
	 * Calcola la somma degli elementi della lista fornita (logica di business
	 * simulata come lenta tramite un'attesa).
	 *
	 * @param list la lista di interi di cui calcolare la somma
	 * @return la somma degli elementi della lista
	 */
	private static int sum(List<Integer> list) {
		log.info("Calculating sum of {}", list);
		Util.sleepSeconds(3);
		return list.stream()
				   .mapToInt(Integer::intValue)
				   .sum();
	}
}
