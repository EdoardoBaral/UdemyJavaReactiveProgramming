package it.baral.sec05;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

/**
 * Mostra l'operatore {@code defaultIfEmpty}, che fornisce un valore di fallback
 * quando il {@code Flux} a monte completa senza emettere alcun elemento.
 */
public class DefaultIfEmpty {

	/**
	 * Esegue la demo relativa a {@link #emptyFlux()}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		emptyFlux();
//		normalFlux();
	}

	/**
	 * Mostra {@code defaultIfEmpty} applicato a un {@code Flux} filtrato fino a
	 * diventare vuoto: viene emesso il valore di default.
	 */
	private static void emptyFlux() {
		Flux.range(1, 10)
			.filter(i -> i > 10)
			.defaultIfEmpty(-1)
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Mostra {@code defaultIfEmpty} applicato a un {@code Flux} non vuoto: il valore
	 * di default viene ignorato perché il flusso emette già degli elementi.
	 */
	private static void normalFlux() {
		Flux.range(1, 10)
			.defaultIfEmpty(-1)
			.subscribe(Util.subscriber());
	}
}
