package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.applications.OrderService;
import it.baral.sec09.applications.User;
import it.baral.sec09.applications.UserService;

/**
 * Dimostra l'operatore {@code flatMap} su {@link reactor.core.publisher.Flux}:
 * per ogni utente emesso recupera in modo asincrono e concorrente il relativo flusso di ordini,
 * appiattendo tutti i risultati in un unico {@code Flux}.
 */
public class FluxFlatMap {

	/**
	 * Punto di ingresso della demo: per ogni utente restituito da {@code UserService.getUsers()}
	 * recupera i relativi ordini tramite {@code flatMap}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		UserService.getUsers()
				   .map(User::id)
				   .flatMap(OrderService::getUserOrders)
				   .subscribe(Util.subscriber());

		Util.sleepSeconds(5);
	}
}
