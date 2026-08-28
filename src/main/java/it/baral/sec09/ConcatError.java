package it.baral.sec09;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Dimostra la differenza tra {@code concatWith} (interrompe subito la concatenazione
 * al primo errore) e {@code Flux.concatDelayError} (esegue comunque le sorgenti
 * successive e posticipa la propagazione dell'errore al termine).
 */
public class ConcatError {

	private static final Logger log = LoggerFactory.getLogger(ConcatError.class);

	/**
	 * Punto di ingresso: seleziona quale comportamento in presenza di errore dimostrare.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
//		concatWith();
		concatDelayError();

		Util.sleep(Duration.ofSeconds(3));
	}

	/**
	 * Sorgente di esempio che emette 1, 2, 3 con un piccolo ritardo tra un elemento
	 * e l'altro.
	 *
	 * @return un {@link Flux} di interi
	 */
	private static Flux<Integer> producer1() {
		return Flux.just(1, 2, 3)
				   .doOnSubscribe(s -> log.info("subscribing to producer1"))
				   .delayElements(Duration.ofMillis(10));
	}

	/**
	 * Sorgente di esempio che emette 4, 5, 6 con un piccolo ritardo tra un elemento
	 * e l'altro.
	 *
	 * @return un {@link Flux} di interi
	 */
	private static Flux<Integer> producer2() {
		return Flux.just(4, 5, 6)
				   .doOnSubscribe(s -> log.info("subscribing to producer2"))
				   .delayElements(Duration.ofMillis(10));
	}

	/**
	 * Sorgente di esempio che termina immediatamente con un errore.
	 *
	 * @return un {@link Flux} che emette un {@link RuntimeException}
	 */
	private static Flux<Integer> producer3() {
		return Flux.error(new RuntimeException("ooops"));
	}

	/**
	 * Concatena {@code producer1}, {@code producer3} (che fallisce) e {@code producer2},
	 * mostrando che con {@code concatWith} l'errore interrompe subito la sequenza e
	 * {@code producer2} non viene mai sottoscritto.
	 */
	private static void concatWith() {
		producer1().concatWith(producer3())
				   .concatWith(producer2())
				   .subscribe(Util.subscriber());
	}
	
	/**
	 * Concatena {@code producer1}, {@code producer3} (che fallisce) e {@code producer2}
	 * tramite {@code Flux.concatDelayError}, mostrando che tutte le sorgenti vengono
	 * comunque sottoscritte e l'errore viene propagato solo al termine.
	 */
	private static void concatDelayError() {
		Flux.concatDelayError(producer1(), producer3(), producer2())
			.subscribe(Util.subscriber());
	}
}
