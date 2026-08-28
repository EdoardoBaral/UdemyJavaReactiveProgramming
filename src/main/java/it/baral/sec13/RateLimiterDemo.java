package it.baral.sec13;

import it.baral.common.Util;
import it.baral.sec13.client.ExternalServiceClient;
import reactor.util.context.Context;

/**
 * Classe dimostrativa che simula 20 chiamate consecutive (una al secondo) di un utente
 * verso {@link ExternalServiceClient#getBook()}, per osservare in pratica l'effetto del
 * {@link it.baral.sec13.RateLimiter} propagato tramite il Context reattivo.
 */
public class RateLimiterDemo {

	/**
	 * Punto di ingresso dell'esempio: esegue 20 chiamate a {@code getBook()} per l'utente
	 * {@code "sam"} (categoria "standard"), distanziate di 1 secondo, e attende poi
	 * il completamento delle chiamate asincrone in corso.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();
		for(int i=1; i<=20; i++) {
			client.getBook()
				  .contextWrite(Context.of("user", "sam"))
				  .subscribe(Util.subscriber());
			Util.sleepSeconds(1);
		}

		Util.sleepSeconds(15);
	}
}
