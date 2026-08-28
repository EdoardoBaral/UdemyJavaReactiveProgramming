package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.applications.OrderService;
import reactor.core.publisher.Flux;

/**
 * Dimostra l'operatore {@code collectList}: raccoglie tutti gli elementi emessi
 * da un {@link Flux} in una singola {@link java.util.List} pubblicata come {@link reactor.core.publisher.Mono}.
 */
public class CollectList {

	/**
	 * Punto di ingresso della demo: recupera gli ordini di 3 utenti e li raccoglie
	 * in un'unica lista tramite {@code collectList}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux.range(1, 3)
			.flatMap(OrderService::getUserOrders)
			.collectList()
			.subscribe(Util.subscriber());

		Util.sleepSeconds(5);
	}
}
