package it.baral.sec05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 * Mostra le varianti del metodo {@code subscribe()} di {@code Flux}, combinate con
 * i callback {@code doOnNext}, {@code doOnComplete} e {@code doOnError} per osservare
 * gli elementi emessi senza passare consumer direttamente a {@code subscribe}.
 */
public class Subscribe {

	private static final Logger log = LoggerFactory.getLogger(Subscribe.class);

	/**
	 * Sottoscrive un {@code Flux} di interi senza argomenti, delegando l'osservazione
	 * degli elementi, del completamento e degli errori ai callback {@code do*} a monte.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux.range(1, 10)
			.doOnNext(i -> log.info("received: {}", i))
			.doOnComplete(() -> log.info("completed"))
			.doOnError(err -> log.error("error: {}", err))
			.subscribe();
	}
}
