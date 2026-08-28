package it.baral.sec02;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Classe dimostrativa della sezione 2 del corso: mostra {@link Mono#fromRunnable(Runnable)},
 * usato per eseguire un'azione con effetto collaterale (senza produrre un
 * valore) quando un {@link Mono} deve completare vuoto invece di emettere un
 * risultato.
 */
public class MonoFromRunnable {

	private static final Logger log = LoggerFactory.getLogger(MonoFromRunnable.class);

	/**
	 * Sottoscrive il {@link Mono} restituito da {@link #getProduceName(int)}
	 * per il prodotto con identificativo 2 (caso non disponibile).
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		getProduceName(2).subscribe(Util.subscriber());
	}

	/**
	 * Restituisce il nome del prodotto se l'identificativo e' 1, altrimenti
	 * notifica l'indisponibilita' del prodotto senza emettere alcun valore.
	 *
	 * @param productId l'identificativo del prodotto richiesto
	 * @return un {@link Mono} con il nome del prodotto, oppure un {@link Mono} vuoto se il prodotto non e' disponibile
	 */
	private static Mono<String> getProduceName(int productId) {
		if(productId == 1) {
			return Mono.fromSupplier(() -> Util.faker().commerce().productName());
		}
		return Mono.fromRunnable(() -> notifyBusiness(productId));
	}

	/**
	 * Logga la notifica al reparto business dell'indisponibilita' del
	 * prodotto indicato.
	 *
	 * @param productId l'identificativo del prodotto non disponibile
	 */
	private static void notifyBusiness(int productId) {
		log.info("Notifying business product is not available: {}", productId);
	}
}
