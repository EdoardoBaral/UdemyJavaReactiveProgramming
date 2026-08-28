package it.baral.sec09.applications;

import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Servizio applicativo di esempio (sezione sec09) che simula la gestione dei saldi
 * degli utenti di una mini-applicazione, tenendo in memoria un'associazione tra
 * identificativo utente e saldo disponibile.
 */
public class PaymentService {

	private static final Map<Integer, Integer> userBalanceTable = Map.of(1 ,100,
																		 2, 200,
																		 3, 300);

	/**
	 * Recupera il saldo disponibile per un utente dato il suo identificativo.
	 *
	 * @param userId identificativo dell'utente
	 * @return un {@code Mono} che emette il saldo dell'utente
	 */
	public static Mono<Integer> getUserBalance(Integer userId) {
		return Mono.fromSupplier(() -> userBalanceTable.get(userId));
	}
}
