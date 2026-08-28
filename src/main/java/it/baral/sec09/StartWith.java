package it.baral.sec09;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

/**
 * Dimostra le varianti dell'operatore {@code startWith}, che antepone valori,
 * iterable o un altro {@link Flux} alla sequenza originale.
 */
public class StartWith {

	private static final Logger log = LoggerFactory.getLogger(StartWith.class);

	/**
	 * Punto di ingresso: seleziona quale variante di {@code startWith} dimostrare.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
//		startWithElements();
//		startWithIterable();
//		startWithPublisher();
		multipleStartWith();

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
	 * Antepone i valori -1 e 0 alla sequenza di {@code producer1}, poi tronca il
	 * risultato ai primi 3 elementi con {@code take}.
	 */
	private static void startWithElements() {
		producer1().startWith(-1, 0)
				   .take(3)
				   .subscribe(Util.subscriber());
	}
	
	/**
	 * Antepone una {@link List} di interi alla sequenza di {@code producer1} tramite
	 * la variante di {@code startWith} che accetta un {@code Iterable}.
	 */
	private static void startWithIterable() {
		producer1().startWith(List.of(-1, 0))
				   .subscribe(Util.subscriber());
	}
	
	/**
	 * Antepone l'intera sequenza di {@code producer2} a quella di {@code producer1}
	 * tramite la variante di {@code startWith} che accetta un {@link Flux}.
	 */
	private static void startWithPublisher() {
		producer1().startWith(producer2())
				   .subscribe(Util.subscriber());
	}
	
	/**
	 * Concatena due {@code startWith} in sequenza (prima {@code producer2}, poi il
	 * valore 1000), mostrando che l'ultimo applicato appare per primo nel risultato.
	 */
	private static void multipleStartWith() {
		producer1().startWith(producer2())
				   .startWith(1000)
				   .subscribe(Util.subscriber());
	}
}
