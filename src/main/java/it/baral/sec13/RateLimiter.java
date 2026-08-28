package it.baral.sec13;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Limitatore di frequenza (rate limiter) basato sul {@link reactor.util.context.Context} reattivo.
 * Mantiene per ogni categoria di utente un numero di tentativi disponibili, ricaricato
 * periodicamente, e permette di far fallire un flusso reattivo quando i tentativi si esauriscono.
 */
public class RateLimiter {

	private static final Map<String, Integer> categoryAttempts = Collections.synchronizedMap(new HashMap<>());

	static {
		refresh();
	}

	/**
	 * Verifica, leggendo la chiave {@code "category"} dal Context reattivo, se è ancora disponibile
	 * un tentativo per la categoria dell'utente corrente. Da usare tipicamente con {@code startWith(...)}
	 * prima dell'operazione da limitare.
	 *
	 * @param <T> il tipo di elemento del Mono restituito (non viene mai effettivamente emesso)
	 * @return un {@link Mono} vuoto se la chiamata è consentita, oppure un {@link Mono} in errore
	 *         con {@link RuntimeException} se il limite è stato superato o la categoria è assente dal Context
	 */
	public static <T> Mono<T> limitCalls() {
		return Mono.deferContextual(ctx -> {
			boolean allowCall = ctx.<String>getOrEmpty("category")
								   .map(RateLimiter::canAllow)
								   .orElse(false);

			return allowCall ? Mono.empty() : Mono.error(new RuntimeException("rate limit exceeded"));
		});
	}

	/**
	 * Consuma, in modo sincronizzato e thread-safe, un tentativo disponibile per la categoria indicata.
	 *
	 * @param category la categoria per cui verificare/consumare un tentativo
	 * @return {@code true} se era disponibile almeno un tentativo (ora decrementato), {@code false} altrimenti
	 */
	private static synchronized boolean canAllow(String category) {
		int attempts = categoryAttempts.getOrDefault(category, 0);
		if(attempts > 0) {
			categoryAttempts.put(category, attempts-1);
			return true;
		}
		return false;
	}

	/**
	 * Avvia un {@link Flux#interval(Duration)} che, ogni 5 secondi (con un primo tick immediato),
	 * ripristina il numero di tentativi disponibili per le categorie {@code "standard"} (2 tentativi)
	 * e {@code "prime"} (3 tentativi).
	 */
	private static void refresh() {
		Flux.interval(Duration.ofSeconds(5))
			.startWith(1L)
			.subscribe(i -> {
				categoryAttempts.put("standard", 2);
				categoryAttempts.put("prime", 3);
			});
	}
}
