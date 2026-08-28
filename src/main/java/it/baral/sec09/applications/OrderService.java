package it.baral.sec09.applications;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Servizio applicativo di esempio (sezione sec09) che simula la gestione degli ordini
 * di una mini-applicazione, tenendo in memoria un'associazione tra identificativo utente
 * e lista dei relativi ordini.
 */
public class OrderService {

	private static final Map<Integer, List<Order>> orderTable = Map.of(
		1, List.of(new Order(1, Util.faker().commerce().productName(), Util.faker().random().nextInt(10, 100)),
				   	   new Order(1, Util.faker().commerce().productName(), Util.faker().random().nextInt(10, 100))),
		2, List.of(new Order(2, Util.faker().commerce().productName(), Util.faker().random().nextInt(10, 100)),
				       new Order(2, Util.faker().commerce().productName(), Util.faker().random().nextInt(10, 100)),
					   new Order(2, Util.faker().commerce().productName(), Util.faker().random().nextInt(10, 100))),
		3, List.of()
	);

	/**
	 * Restituisce il flusso degli ordini associati a un utente, emessi con un ritardo
	 * artificiale tra un elemento e l'altro e tracciati tramite logger.
	 *
	 * @param userId identificativo dell'utente di cui recuperare gli ordini
	 * @return un {@code Flux} che emette gli ordini dell'utente
	 */
	public static Flux<Order> getUserOrders(Integer userId) {
		return Flux.fromIterable(orderTable.get(userId))
				   .delayElements(Duration.ofMillis(500))
				   .transform(Util.fluxLogger("order-for-user-"+ userId));
	}
}
