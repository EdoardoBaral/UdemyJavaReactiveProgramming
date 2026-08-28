package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

/**
 * Classe dimostrativa della sezione 3 del corso: mostra il comportamento
 * base di {@link Flux#just(Object[])}, che emette in sequenza i valori
 * forniti e poi completa.
 */
public class FluxJust {

	/**
	 * Sottoscrive un {@link Flux} creato con {@code just} da una sequenza
	 * fissa di interi.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux.just(1, 2, 3, 4, 5)
			.subscribe(Util.subscriber());
	}
}
