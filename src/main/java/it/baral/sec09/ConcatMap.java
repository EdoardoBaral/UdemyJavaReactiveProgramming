package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.assignment.ExternalServiceClient;
import reactor.core.publisher.Flux;

/**
 * Dimostra l'operatore {@code concatMap} su {@link Flux}: a differenza di {@code flatMap},
 * elabora le sorgenti generate in sequenza, preservando l'ordine di emissione originale.
 */
public class ConcatMap {

	/**
	 * Punto di ingresso della demo: recupera in sequenza i dettagli di 10 prodotti
	 * tramite {@code concatMap}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		ExternalServiceClient serviceClient = new ExternalServiceClient();

		Flux.range(1, 10)
			.concatMap(serviceClient::getProduct)
			.subscribe(Util.subscriber());

		Util.sleepSeconds(10);
	}
}
