package it.baral.sec06.assignment;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementazione di {@link OrderProcessor} che accumula i ricavi totali per
 * categoria di prodotto, sommando il prezzo di ogni ordine ricevuto.
 */
public class RevenueService implements OrderProcessor {

	private final Map<String, Integer> db = new HashMap<>();

	/**
	 * Somma il prezzo dell'ordine al ricavo accumulato per la sua categoria.
	 *
	 * @param order l'ordine il cui prezzo va sommato al ricavo della categoria
	 */
	@Override
	public void consume(Order order) {
		Integer currentRevenue = db.getOrDefault(order.category(), 0);
		Integer updatedRevenue = currentRevenue + order.price();
		db.put(order.category(), updatedRevenue);
	}

	/**
	 * Espone lo stato corrente dei ricavi per categoria ogni 2 secondi.
	 *
	 * @return un {@code Flux} che emette la rappresentazione testuale della mappa dei ricavi ogni 2 secondi
	 */
	@Override
	public Flux<String> stream() {
		return Flux.interval(Duration.ofSeconds(2))
				   .map(i -> this.db.toString());
	}
}
