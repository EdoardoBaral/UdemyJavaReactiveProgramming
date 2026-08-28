package it.baral.sec09;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Dimostra {@code Flux.merge}/{@code mergeWith}, che sottoscrive pi&ugrave; sorgenti
 * contemporaneamente ed emette i valori nell'ordine in cui arrivano, a differenza
 * della concatenazione che le esegue in sequenza.
 */
public class Merge {

	private static final Logger log = LoggerFactory.getLogger(Merge.class);

	/**
	 * Punto di ingresso: seleziona quale variante di merge dimostrare.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
//		merge();
		mergeWith();

		Util.sleepSeconds(5);
	}

	/**
	 * Sorgente di esempio che emette 1, 2, 3 con un piccolo ritardo tra un elemento
	 * e l'altro.
	 *
	 * @return un {@link Flux} di interi
	 */
	private static Flux<Integer> producer1() {
		return Flux.just(1, 2, 3)
				   .transform(Util.fluxLogger("producer1"))
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
				   .transform(Util.fluxLogger("producer2"))
				   .delayElements(Duration.ofMillis(10));
	}

	/**
	 * Sorgente di esempio che emette 7, 8, 9 con un piccolo ritardo tra un elemento
	 * e l'altro.
	 *
	 * @return un {@link Flux} di interi
	 */
	private static Flux<Integer> producer3() {
		return Flux.just(7, 8, 9)
				   .transform(Util.fluxLogger("producer3"))
				   .delayElements(Duration.ofMillis(10));
	}

	/**
	 * Unisce le tre sorgenti tramite {@code Flux.merge}, sottoscritte tutte
	 * contemporaneamente, e ne preleva solo i primi 2 valori emessi.
	 */
	private static void merge() {
		Flux.merge(producer1(), producer2(), producer3())
			.take(2)
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Unisce le tre sorgenti in sequenza tramite l'operatore d'istanza
	 * {@code mergeWith}, equivalente a {@code Flux.merge}, e ne preleva solo i
	 * primi 2 valori emessi.
	 */
	private static void mergeWith() {
		producer1().mergeWith(producer2())
				   .mergeWith(producer3())
				   .take(2)
				   .subscribe(Util.subscriber());
	}
}
