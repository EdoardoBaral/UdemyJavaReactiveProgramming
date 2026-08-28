package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

/**
 * Classe dimostrativa della sezione 3 del corso: mostra i due esiti
 * particolari di un {@link Flux} tramite {@link Flux#empty()} (completamento
 * immediato senza elementi) e {@link Flux#error(Throwable)} (terminazione
 * immediata con errore).
 */
public class FluxEmptyError {

	/**
	 * Sottoscrive un {@link Flux} vuoto e uno in errore.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux.empty()
			.subscribe(Util.subscriber());
		
		Flux.error(new Exception("Oops"))
			.subscribe(Util.subscriber());
	}
}
