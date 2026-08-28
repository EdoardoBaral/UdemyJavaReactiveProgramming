package it.baral.sec05;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

/**
 * Mostra l'operatore {@code switchIfEmpty}, che sostituisce l'intero {@code Flux}
 * a monte con uno di fallback quando esso completa senza emettere elementi.
 */
public class SwitchIfEmpty {

	/**
	 * Esegue la demo relativa a {@link #switchIfEmpty()}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		normalFlux();
		switchIfEmpty();
	}

	/**
	 * Mostra {@code switchIfEmpty} applicato a un {@code Flux} non vuoto: il flusso
	 * di fallback viene ignorato perché il flusso a monte emette già degli elementi.
	 */
	private static void normalFlux() {
		Flux.range(1, 10)
			.filter(i -> i < 6)
			.switchIfEmpty(fallback())
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Mostra {@code switchIfEmpty} applicato a un {@code Flux} filtrato fino a
	 * diventare vuoto: viene sottoscritto il {@code Flux} di fallback.
	 */
	private static void switchIfEmpty() {
		Flux.range(1, 10)
			.filter(i -> i > 10)
			.switchIfEmpty(fallback())
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Crea il {@code Flux} di fallback usato dagli esempi di {@code switchIfEmpty}.
	 *
	 * @return un {@code Flux} che emette il singolo valore -1
	 */
	private static Flux<Integer> fallback() {
		return Flux.just(-1);
	}
}
