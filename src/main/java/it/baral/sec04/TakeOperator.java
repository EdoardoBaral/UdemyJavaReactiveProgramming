package it.baral.sec04;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

/**
 * Confronta gli operatori di troncamento di un {@code Flux}: {@code take}, {@code takeWhile}
 * e {@code takeUntil}, ciascuno con una diversa condizione di terminazione anticipata.
 */
public class TakeOperator {

	/**
	 * Esegue la demo relativa a {@link #takeUntil()}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		//take();
		//takeWhile();
		takeUntil();
	}

	/**
	 * Mostra {@code take(3)}: interrompe il flusso dopo i primi 3 elementi emessi.
	 */
	private static void take() {
		Flux.range(1, 10)
			.log("take")
			.take(3)
			.log("sub")
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Mostra {@code takeWhile(i -> i < 6)}: emette gli elementi finché la condizione
	 * è vera, escludendo l'elemento che la fa fallire.
	 */
	private static void takeWhile() {
		Flux.range(1, 10)
			.log("take")
			.takeWhile(i -> i < 6)
			.log("sub")
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Mostra {@code takeUntil(i -> i == 6)}: emette gli elementi finché la condizione
	 * non si verifica, includendo l'elemento che la soddisfa.
	 */
	private static void takeUntil() {
		Flux.range(1, 10)
			.log("take")
			.takeUntil(i -> i == 6)
			.log("sub")
			.subscribe(Util.subscriber());
	}
}
