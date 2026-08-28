package it.baral.sec06.assignment;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementazione di {@link OrderProcessor} che tiene traccia della giacenza residua
 * per categoria di prodotto, partendo da una scorta iniziale di 500 unità e sottraendo
 * la quantità di ogni ordine ricevuto.
 */
public class InventoryService implements OrderProcessor {

	private final Map<String, Integer> db = new HashMap<>();

	/**
	 * Sottrae la quantità dell'ordine dalla giacenza residua della sua categoria,
	 * partendo da una scorta iniziale di 500 unità se la categoria non è ancora presente.
	 *
	 * @param order l'ordine la cui quantità va sottratta dalla giacenza della categoria
	 */
	@Override
	public void consume(Order order) {
		Integer currentInventory = db.getOrDefault(order.category(), 500);
		Integer updatedInventory = currentInventory - order.quantity();
		db.put(order.category(), updatedInventory);
	}

	/**
	 * Espone lo stato corrente della giacenza per categoria ogni 2 secondi.
	 *
	 * @return un {@code Flux} che emette la rappresentazione testuale della mappa delle giacenze ogni 2 secondi
	 */
	@Override
	public Flux<String> stream() {
		return Flux.interval(Duration.ofSeconds(2))
				   .map(i -> this.db.toString());
	}
}
