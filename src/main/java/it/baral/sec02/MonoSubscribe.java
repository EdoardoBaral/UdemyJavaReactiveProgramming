package it.baral.sec02;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Classe dimostrativa della sezione 2 del corso: illustra le diverse varianti
 * del metodo {@code subscribe} di {@link Mono} (con consumer per elemento,
 * errore, completamento e/o gestione manuale della {@link Subscription}),
 * inclusa la propagazione degli errori sollevati durante gli operatori come
 * {@code map}.
 */
public class MonoSubscribe {

	private static final Logger log = LoggerFactory.getLogger(MonoSubscribe.class);

	/**
	 * Esegue diverse sottoscrizioni a {@link Mono} usando varianti di
	 * {@code subscribe} via via piu' complete, fino alla gestione manuale
	 * della richiesta tramite {@link Subscription}, includendo un caso in cui
	 * l'operatore {@code map} solleva un'eccezione.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 * @throws Exception non sollevata in pratica, dichiarata per uniformita' con gli altri esempi
	 */
	public static void main(String[] args) throws Exception {
		Mono<Integer> mono = Mono.just(1);
		mono.subscribe(i -> log.info("Received: {}", i),
					   error -> log.error("Error: ", error),
					   () -> log.info("Completed"));
		System.out.println();
		
		Mono<Integer> mono2 = Mono.just(1);
		mono2.subscribe(i -> log.info("Received: {}", i),
					    error -> log.error("Error: ", error),
					    () -> log.info("Completed"),
					    Subscription::cancel);
		
		Mono<Integer> mono3 = Mono.just(1);
		mono3.subscribe(i -> log.info("Received: {}", i),
						error -> log.error("Error: ", error),
						() -> log.info("Completed"),
						subscription -> subscription.request(1));
		System.out.println();
		
		Mono<String> mono4 = Mono.just(1)
								 .map(i -> i + "a");
		mono4.subscribe(i -> log.info("Received: {}", i),
						error -> log.error("Error: ", error),
						() -> log.info("Completed"),
						subscription -> subscription.request(1));
		System.out.println();
		
		Mono<Integer> mono5 = Mono.just(1)
								  .map(i -> i/0);
		mono5.subscribe(i -> log.info("Received: {}", i),
						error -> log.error("Error: ", error),
						() -> log.info("Completed"),
						subscription -> subscription.request(1));
		System.out.println();
	}
}
