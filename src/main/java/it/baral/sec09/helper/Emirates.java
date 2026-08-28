package it.baral.sec09.helper;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Simula il servizio di ricerca voli della compagnia aerea Emirates, usato negli
 * esempi di {@code merge} della sezione 09.
 */
public class Emirates {

	private static final String AIRLINE = "Emirates";

	/**
	 * Genera un numero casuale (tra 2 e 9) di offerte di volo Emirates, emesse a
	 * intervalli casuali per simulare la latenza di un servizio reale.
	 *
	 * @return un {@link Flux} di {@link Flight} con prezzo casuale
	 */
	public static Flux<Flight> getFlights() {
		return Flux.range(1, Util.faker().random().nextInt(2, 10))
				   .delayElements(Duration.ofMillis(Util.faker().random().nextInt(200, 1000)))
				   .map(i -> new Flight(AIRLINE, Util.faker().random().nextInt(300, 1000)))
				   .transform(Util.fluxLogger(AIRLINE));
	}
}
