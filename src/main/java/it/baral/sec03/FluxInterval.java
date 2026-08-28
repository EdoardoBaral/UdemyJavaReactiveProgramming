package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Classe dimostrativa della sezione 3 del corso: mostra {@link Flux#interval(Duration)},
 * che emette un valore incrementale (long) a intervalli regolari, qui
 * trasformato in un nome fittizio ad ogni tick.
 */
public class FluxInterval {

	/**
	 * Sottoscrive un {@link Flux} basato su un intervallo di 500 millisecondi,
	 * attendendo poi qualche secondo affinche' alcuni tick possano essere
	 * osservati prima della terminazione del programma.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux.interval(Duration.ofMillis(500))
			.map(x -> Util.faker().name().name())
			.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
}
