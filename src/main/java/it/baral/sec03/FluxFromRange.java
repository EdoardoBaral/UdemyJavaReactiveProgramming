package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

/**
 * Classe dimostrativa della sezione 3 del corso: mostra la creazione di un
 * {@link Flux} di interi consecutivi tramite {@link Flux#range(int, int)},
 * eventualmente trasformati con {@code map}.
 */
public class FluxFromRange {

	/**
	 * Sottoscrive due {@link Flux} creati con {@code range}: uno con gli
	 * interi grezzi, l'altro mappato a nomi fittizi.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux.range(1, 10)
			.subscribe(Util.subscriber());
		
		Flux.range(1, 10)
			.map(x -> Util.faker().name().name())
			.subscribe(Util.subscriber());
	}
}
