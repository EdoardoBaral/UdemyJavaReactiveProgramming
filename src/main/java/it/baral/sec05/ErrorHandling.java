package it.baral.sec05;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 * Confronta i principali operatori di gestione degli errori di Reactor:
 * {@code onErrorReturn}, {@code onErrorResume}, {@code onErrorComplete} e
 * {@code onErrorContinue}, con e senza filtro sul tipo di eccezione.
 */
public class ErrorHandling {

	private static final Logger log = LoggerFactory.getLogger(ErrorHandling.class);

	/**
	 * Esegue la demo relativa a {@link #onErrorContinue()}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		onErrorReturnSimple();
//		onErrorReturnWithException();
//		onErrorResume();
//		onErrorResumeWithException();
//		onErrorComplete();
		onErrorContinue();
	}

	/**
	 * Mostra {@code onErrorReturn} usato per sostituire qualsiasi errore con un valore fisso.
	 */
	private static void onErrorReturnSimple() {
		Flux.range(1, 10)
			.map(i -> i == 5 ? 5/0 : i)
			.onErrorReturn(-1)
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Mostra {@code onErrorReturn} filtrato su {@link ArithmeticException}, sostituendo
	 * solo quel tipo di errore con un valore fisso.
	 */
	private static void onErrorReturnWithException() {
		Flux.range(1, 10)
			.map(i -> i == 5 ? 5/0 : i)
			.onErrorReturn(ArithmeticException.class, -1)
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Mostra {@code onErrorResume} usato per sostituire qualsiasi errore con un
	 * {@code Flux} di fallback.
	 */
	private static void onErrorResume() {
		Flux.range(1, 10)
			.map(i -> i == 5 ? 5/0 : i)
			.onErrorResume(ex -> fallback())
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Mostra {@code onErrorResume} filtrato su {@link ArithmeticException}, sostituendo
	 * solo quel tipo di errore con un {@code Flux} di fallback.
	 */
	private static void onErrorResumeWithException() {
		Flux.range(1, 10)
			.map(i -> i == 5 ? 5/0 : i)
			.onErrorResume(ArithmeticException.class, ex -> fallback())
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Mostra {@code onErrorComplete} usato per trasformare un errore in un
	 * completamento silenzioso del flusso.
	 */
	private static void onErrorComplete() {
		Flux.range(1, 10)
			.map(i -> i == 5 ? 5/0 : i)
			.onErrorComplete()
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Mostra {@code onErrorContinue} usato per ignorare l'elemento che ha causato
	 * l'errore, loggarlo e proseguire con gli elementi successivi.
	 */
	private static void onErrorContinue() {
		Flux.range(1, 10)
			.map(i -> i == 5 ? 5/0 : i)
			.onErrorContinue((ex, item) -> log.error("--> {}", item, ex))
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Crea un {@code Flux} di fallback usato dagli esempi di {@code onErrorResume}.
	 *
	 * @return un {@code Flux} di 5 numeri interi casuali
	 */
	private static Flux<Integer> fallback() {
		return Flux.range(1, 5)
				   .map(i -> Util.faker().random().nextInt(1, 10));
	}
}
