package it.baral.sec09.assignment;

import it.baral.common.Util;
import it.baral.sec09.applications.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Esercizi riepilogativi della sezione sec09 sugli operatori di trasformazione asincrona
 * ({@code flatMap}, {@code zip}) applicati a un client HTTP esterno e ai servizi applicativi
 * di esempio (utenti, saldi, ordini).
 */
public class Assignment {

	/**
	 * Rappresenta le informazioni aggregate di un utente: dati anagrafici, saldo e ordini effettuati.
	 *
	 * @param userId identificativo dell'utente
	 * @param username nome utente
	 * @param balance saldo disponibile dell'utente
	 * @param orders lista degli ordini effettuati dall'utente
	 */
	record UserInformation(Integer userId, String username, Integer balance, List<Order> orders) {};

	/**
	 * Punto di ingresso: esegue il secondo esercizio (recupero delle informazioni aggregate utente).
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		assignment1();
		assignment2();
	}

	/**
	 * Primo esercizio: recupera in sequenza i dettagli di 10 prodotti tramite {@link ExternalServiceClient}.
	 */
	private static void assignment1() {
		ExternalServiceClient client = new ExternalServiceClient();

		for(int i=1; i<=10; i++) {
			client.getProduct(i)
				.subscribe(Util.subscriber());
		}

		Util.sleepSeconds(5);
	}

	/**
	 * Secondo esercizio: per ogni utente recupera le informazioni aggregate (saldo e ordini)
	 * tramite {@code flatMap}.
	 */
	private static void assignment2() {
		UserService.getUsers()
			.flatMap(Assignment::getUserInformation)
			.subscribe(Util.subscriber());

		Util.sleepSeconds(5);
	}

	/**
	 * Compone le informazioni aggregate di un utente combinando saldo e ordini,
	 * recuperati in parallelo tramite {@code Mono.zip}.
	 *
	 * @param user utente di cui recuperare le informazioni aggregate
	 * @return un {@code Mono} che emette le informazioni aggregate dell'utente
	 */
	private static Mono<UserInformation> getUserInformation(User user) {
		return Mono.zip(PaymentService.getUserBalance(user.id()),
						OrderService.getUserOrders(user.id()).collectList())
				   .map(t -> new UserInformation(user.id(), user.username(), t.getT1(), t.getT2()));
	}
}
