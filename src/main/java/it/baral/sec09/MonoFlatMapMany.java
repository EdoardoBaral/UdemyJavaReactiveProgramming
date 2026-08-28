package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.applications.OrderService;
import it.baral.sec09.applications.UserService;

/**
 * Dimostra l'operatore {@code flatMapMany} su {@link reactor.core.publisher.Mono}:
 * trasforma il singolo valore emesso da un {@code Mono} in un {@link reactor.core.publisher.Flux}
 * di più elementi.
 */
public class MonoFlatMapMany {

	/**
	 * Punto di ingresso della demo: recupera l'identificativo dell'utente "sam"
	 * e lo usa per ottenere il flusso dei suoi ordini tramite {@code flatMapMany}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		UserService.getUserId("sam")
				   .flatMapMany(OrderService::getUserOrders)
				   .subscribe(Util.subscriber());

		Util.sleepSeconds(2);
	}
}
