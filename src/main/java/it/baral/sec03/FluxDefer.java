package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Classe dimostrativa della sezione 3 del corso: mostra {@link Flux#defer(java.util.function.Supplier)},
 * l'equivalente per {@link Flux} di {@code Mono.defer}, che rimanda la
 * creazione del publisher effettivo al momento della sottoscrizione.
 */
public class FluxDefer {

	/**
	 * Confronta un {@link Flux} creato direttamente da una lista con uno
	 * creato tramite {@code defer}, che ricalcola la lista ad ogni
	 * sottoscrizione.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux.fromIterable(List.of(1, 2, 3, 4, 5))
			.subscribe(Util.subscriber());
		
		Flux.defer(() -> Flux.fromIterable(List.of(1, 2, 3, 4, 5)))
			.subscribe(Util.subscriber());
	}
}
