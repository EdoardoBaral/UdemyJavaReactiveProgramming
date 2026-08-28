package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Classe dimostrativa della sezione 3 del corso: mostra la relazione tra
 * {@link Mono} e {@link Flux}, in particolare come un {@link Mono} possa
 * essere convertito in {@link Flux} tramite {@link Flux#from(org.reactivestreams.Publisher)}
 * e come {@link Flux#next()} converta invece un {@link Flux} nel {@link Mono}
 * del suo primo elemento.
 */
public class MonoFlux {

	/**
	 * Converte tre {@link Mono} (valore, vuoto, errore) in {@link Flux} e li
	 * sottoscrive tramite {@link #save(Flux)}; sottoscrive inoltre un
	 * {@link Mono} ottenuto da {@code Flux.range(...).next()}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		save(Flux.from(getUsername(1)));
		save(Flux.from(getUsername(2)));
		save(Flux.from(getUsername(3)));

		Flux.range(1, 10)
			.next()
			.subscribe(Util.subscriber());
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

	/**
	 * Sottoscrive il {@link Flux} fornito, loggandone gli elementi.
	 *
	 * @param flux il flusso da sottoscrivere
	 */
	private static void save(Flux<String> flux) {
		flux.subscribe(Util.subscriber());
	}
}
