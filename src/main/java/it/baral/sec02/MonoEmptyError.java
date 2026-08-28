package it.baral.sec02;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Classe dimostrativa della sezione 2 del corso: mostra i tre possibili esiti
 * di un {@link Mono} (valore emesso, completamento vuoto tramite
 * {@link Mono#empty()}, errore tramite {@link Mono#error(Throwable)}) e come
 * vengono gestiti da un subscriber.
 */
public class MonoEmptyError {

	private static final Logger log = LoggerFactory.getLogger(MonoEmptyError.class);

	/**
	 * Sottoscrive quattro {@link Mono} restituiti da {@link #getUsername(int)}
	 * per osservare i tre possibili esiti (valore, vuoto, errore).
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		getUsername(1).subscribe(Util.subscriber());
		System.out.println();
		
		getUsername(2).subscribe(Util.subscriber("Subscriber1"));
		System.out.println();
		
		getUsername(3).subscribe(Util.subscriber("Subscriber2"));
		System.out.println();
		
		getUsername(3).subscribe(System.out::println);
		System.out.println();
	}
	
	/**
	 * Restituisce un {@link Mono} diverso in base all'identificativo utente:
	 * un nome valido, un {@link Mono} vuoto oppure un {@link Mono} in errore.
	 *
	 * @param userId l'identificativo dell'utente da cercare
	 * @return un {@link Mono} contenente il nome utente, vuoto o in errore a seconda del caso
	 */
	private static Mono<String> getUsername(int userId) {
		return switch(userId) {
			case 1 -> Mono.just("Edoardo");
			case 2 -> Mono.empty();
			default -> Mono.error(new IllegalArgumentException("Invalid input"));
		};
	}
}
