package it.baral.sec11;

import it.baral.common.Util;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe dimostrativa sull'operatore {@code repeat}, che trasforma un
 * {@link Mono} in un {@link reactor.core.publisher.Flux} ri-sottoscrivendo la sorgente originale
 * un numero di volte definito, secondo una condizione, oppure guidato da
 * un {@link reactor.core.publisher.Flux} di trigger (repeat/repeatWhen).
 */
public class Repeat {

	/**
	 * Punto di ingresso dell'applicazione. Decommentare uno dei metodi
	 * di demo per eseguire il relativo scenario.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		demoRepeatNumIteration();
//		demoRepeatWithCondition();
//		demoRepeatWithSupplier();
//		demoRepeatWhen();
		demoRepeatWhenWithEnd();
	}

	/**
	 * Mostra come {@code repeat(int)} trasformi il {@link Mono} originale in
	 * un {@link Flux}, ripetendo la sottoscrizione per il numero di volte
	 * indicato oltre alla prima emissione.
	 */
	private static void demoRepeatNumIteration() {
		//Il Mono originale diventa un Flux con l'applicazione dell'operatore repeat
		getCountryName().repeat(3) //Vengono generati 4 elementi perché l'argomento di repeat indica il numero di ripetizioni successive alla prima emissione di default
						.subscribe(Util.subscriber());
	}

	/**
	 * Mostra come combinare {@code repeat()} (ripetizione illimitata) con
	 * {@code takeUntil} per interrompere la ripetizione al verificarsi di
	 * una condizione sul valore emesso.
	 */
	private static void demoRepeatWithCondition() {
		getCountryName().repeat()
						.takeUntil("Italy"::equalsIgnoreCase)
						.subscribe(Util.subscriber());
	}

	/**
	 * Mostra come {@code repeat(BooleanSupplier)} permetta di decidere ad
	 * ogni iterazione, tramite un contatore esterno, se ripetere ancora
	 * la sottoscrizione.
	 */
	private static void demoRepeatWithSupplier() {
		AtomicInteger count = new AtomicInteger(0);
		getCountryName().repeat(() -> count.incrementAndGet() < 5)
						.subscribe(Util.subscriber());
	}

	/**
	 * Mostra come {@code repeatWhen} guidi la ripetizione tramite un
	 * {@link Flux} di trigger costruito ritardando gli elementi, senza
	 * un limite esplicito al numero di ripetizioni.
	 */
	private static void demoRepeatWhen() {
		getCountryName().repeatWhen(flux -> flux.delayElements(Duration.ofMillis(500)))
						.subscribe(Util.subscriber());
		Util.sleepSeconds(10);
	}

	/**
	 * Mostra come {@code repeatWhen} possa essere combinato con {@code take}
	 * sul Flux di trigger per limitare il numero complessivo di ripetizioni.
	 */
	private static void demoRepeatWhenWithEnd() {
		getCountryName().repeatWhen(flux -> flux.delayElements(Duration.ofMillis(500))
																				  .take(5))
			.subscribe(Util.subscriber());
		Util.sleepSeconds(10);
	}

	/**
	 * Genera un {@link Mono} che, ad ogni sottoscrizione, produce il nome
	 * di un paese casuale.
	 *
	 * @return un {@link Mono} che emette il nome di un paese generato casualmente
	 */
	private static Mono<String> getCountryName() {
		return Mono.fromSupplier(() -> Util.faker().country().name());
	}
}
