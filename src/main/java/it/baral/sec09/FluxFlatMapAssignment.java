package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.assignment.ExternalServiceClient;
import reactor.core.publisher.Flux;

/**
 * Esercizio sull'operatore {@code flatMap} su {@link Flux}: per ogni identificativo prodotto
 * generato recupera i dettagli del prodotto tramite {@link ExternalServiceClient}, limitando
 * la concorrenza delle chiamate simultanee.
 */
public class FluxFlatMapAssignment {

	/**
	 * Punto di ingresso dell'esercizio: recupera i dettagli di 10 prodotti con al massimo
	 * 3 chiamate concorrenti tramite {@code flatMap(mapper, concurrency)}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		ExternalServiceClient serviceClient = new ExternalServiceClient();

		Flux.range(1, 10)
			.flatMap(serviceClient::getProduct, 3)
			.subscribe(Util.subscriber());

		Util.sleepSeconds(10);
	}
}
