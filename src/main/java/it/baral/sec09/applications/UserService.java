package it.baral.sec09.applications;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Servizio applicativo di esempio (sezione sec09) che simula la gestione degli utenti
 * di una mini-applicazione, tenendo in memoria un'associazione tra nome utente e identificativo.
 */
public class UserService {

	private static final Map<String, Integer> userTable = Map.of("sam", 1,
																 "mike", 2,
																 "jake", 3);

	/**
	 * Restituisce tutti gli utenti presenti nella tabella in memoria.
	 *
	 * @return un {@code Flux} che emette tutti gli utenti disponibili
	 */
	public static Flux<User> getUsers() {
		return Flux.fromIterable(userTable.entrySet())
				   .map(entry -> new User(entry.getValue(), entry.getKey()));
	}

	/**
	 * Recupera l'identificativo numerico associato a un nome utente.
	 *
	 * @param username nome utente da cercare
	 * @return un {@code Mono} che emette l'identificativo dell'utente
	 */
	public static Mono<Integer> getUserId(String username) {
		return Mono.fromSupplier(() -> userTable.get(username));
	}
}
