package it.baral.sec09.helper;

import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Simula un meta-motore di ricerca voli (come Kayak) che aggrega, tramite
 * {@code Flux.merge}, le offerte di pi&ugrave; compagnie aeree in un unico flusso.
 */
public class Kayak {

	/**
	 * Unisce le offerte di volo di Emirates, Qatar Airways e American Airlines in
	 * un unico {@link Flux}, interrompendo la ricerca dopo 2 secondi.
	 *
	 * @return un {@link Flux} di {@link Flight} provenienti da tutte le compagnie
	 */
	public static Flux<Flight> getFlights() {
		return Flux.merge(Emirates.getFlights(), QatarAirways.getFlights(), AmericanAirlines.getFlights())
				   .take(Duration.ofSeconds(2));
	}
}
