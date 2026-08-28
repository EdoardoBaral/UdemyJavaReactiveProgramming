package it.baral.sec09.helper;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Simula il servizio di ricerca voli della compagnia aerea Qatar Airways, usato
 * negli esempi di {@code merge} della sezione 09.
 */
public class QatarAirways {

	private static final String AIRLINE = "Qatar Airways";

	/**
	 * Genera un numero casuale (tra 3 e 4) di offerte di volo Qatar Airways, emesse a
	 * intervalli casuali per simulare la latenza di un servizio reale.
	 *
	 * @return un {@link Flux} di {@link Flight} con prezzo casuale
	 */
	public static Flux<Flight> getFlights() {
		return Flux.range(1, Util.faker().random().nextInt(3, 5))
				   .delayElements(Duration.ofMillis(Util.faker().random().nextInt(300, 800)))
				   .map(i -> new Flight(AIRLINE, Util.faker().random().nextInt(400, 900)))
				   .transform(Util.fluxLogger(AIRLINE));
	}
}
