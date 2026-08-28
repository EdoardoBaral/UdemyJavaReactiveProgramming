package it.baral.sec09;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Dimostra le varianti dell'operatore {@code concatWith}/{@code Flux.concat}, che
 * concatena sequenze in ordine, sottoscrivendosi a ciascuna solo dopo il completamento
 * della precedente.
 */
public class ConcatWith {

	private static final Logger log = LoggerFactory.getLogger(ConcatWith.class);

	/**
	 * Punto di ingresso: seleziona quale variante di concatenazione dimostrare.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
//		concatWithValues();
//		concatWithPublisher();
//		multipleConcatWith();
		concat();

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
	 * Concatena i valori -1 e 0 dopo la sequenza di {@code producer1} tramite
	 * {@code concatWithValues}.
	 */
	private static void concatWithValues() {
		producer1().concatWithValues(-1, 0)
				   .subscribe(Util.subscriber());
	}
	
	/**
	 * Concatena l'intera sequenza di {@code producer2} dopo quella di {@code producer1}
	 * tramite {@code concatWith}.
	 */
	private static void concatWithPublisher() {
		producer1().concatWith(producer2())
				   .subscribe(Util.subscriber());
	}
	
	/**
	 * Concatena {@code producer2} e poi il valore 1000 in coda a {@code producer1},
	 * mostrando l'ordine di emissione risultante dalla catena di concatenazioni.
	 */
	private static void multipleConcatWith() {
		producer1().concatWith(producer2())
				   .concatWithValues(1000)
				   .subscribe(Util.subscriber());
	}
	
	/**
	 * Concatena {@code producer1} e {@code producer2} tramite il metodo statico
	 * {@code Flux.concat}, equivalente all'operatore d'istanza {@code concatWith}.
	 */
	private static void concat() {
		Flux.concat(producer1(), producer2())
			.subscribe(Util.subscriber());
	}
}
