package it.baral.sec09;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Dimostra l'operatore {@code Flux.zip}, che combina l'ennesimo elemento di ciascuna
 * sorgente in un'unica tupla, emettendo un risultato solo quando tutte le sorgenti
 * hanno prodotto il valore corrispondente (al ritmo della pi&ugrave; lenta).
 */
public class Zip {

	/**
	 * Rappresenta un'auto assemblata combinando un componente per ciascuna delle tre
	 * sorgenti zippate.
	 *
	 * @param body   il componente carrozzeria
	 * @param engine il componente motore
	 * @param tires  il componente pneumatici
	 */
	private record Car(String body, String engine, String tires) {}

	/**
	 * Avvia la dimostrazione dello zip tra le tre sorgenti di componenti.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
		zip();

		Util.sleepSeconds(5);
	}

	/**
	 * Sorgente di esempio che emette 5 componenti "body" a intervalli di 100ms.
	 *
	 * @return un {@link Flux} di stringhe
	 */
	private static Flux<String> body() {
		return Flux.range(1, 5)
				   .map(i -> "body-"+ i)
				   .delayElements(Duration.ofMillis(100));
	}

	/**
	 * Sorgente di esempio che emette 3 componenti "engine" a intervalli di 200ms.
	 *
	 * @return un {@link Flux} di stringhe
	 */
	private static Flux<String> engine() {
		return Flux.range(1, 3)
				   .map(i -> "engine-"+ i)
				   .delayElements(Duration.ofMillis(200));
	}

	/**
	 * Sorgente di esempio che emette 10 componenti "tires" a intervalli di 75ms.
	 *
	 * @return un {@link Flux} di stringhe
	 */
	private static Flux<String> tires() {
		return Flux.range(1, 10)
				   .map(i -> "tires-"+ i)
				   .delayElements(Duration.ofMillis(75));
	}

	/**
	 * Combina le tre sorgenti di componenti con {@code Flux.zip} e assembla ogni
	 * tupla risultante in un'istanza di {@link Car}.
	 */
	private static void zip() {
		Flux.zip(body(), engine(), tires())
			.map(i -> new Car(i.getT1(), i.getT2(), i.getT3()))
			.subscribe(Util.subscriber());
	}
}
