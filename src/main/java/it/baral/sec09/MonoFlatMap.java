package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.applications.UserService;
import reactor.core.publisher.Mono;

/**
 * Dimostra l'operatore {@code flatMap} su {@link Mono}: trasforma il valore emesso
 * da un {@code Mono} in un nuovo {@code Mono}, appiattendo il risultato in un unico flusso asincrono.
 */
public class MonoFlatMap {

	/**
	 * Punto di ingresso della demo: recupera l'identificativo dell'utente "sam"
	 * e lo usa per comporre un messaggio di saluto tramite {@code flatMap}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		UserService.getUserId("sam")
				   .flatMap(userId -> Mono.fromSupplier(() -> "Hello "+ userId))
				   .subscribe(Util.subscriber());
	}
}
