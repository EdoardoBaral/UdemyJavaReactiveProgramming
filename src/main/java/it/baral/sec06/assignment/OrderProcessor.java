package it.baral.sec06.assignment;

import reactor.core.publisher.Flux;

/**
 * Interfaccia comune per i servizi che elaborano gli ordini in arrivo dallo stream
 * hot condiviso e ne espongono lo stato aggregato come flusso periodico.
 */
public interface OrderProcessor {

	/**
	 * Elabora un singolo ordino, aggiornando lo stato interno del servizio.
	 *
	 * @param order l'ordine da elaborare
	 */
	void consume(Order order);

	/**
	 * Espone periodicamente lo stato aggregato corrente del servizio.
	 *
	 * @return un {@code Flux} che emette periodicamente la rappresentazione dello stato interno
	 */
	Flux<String> stream();
}
