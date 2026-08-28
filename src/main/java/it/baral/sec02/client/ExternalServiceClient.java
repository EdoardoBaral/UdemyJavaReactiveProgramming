package it.baral.sec02.client;

import it.baral.common.AbstractHttpClient;
import reactor.core.publisher.Mono;

/**
 * Client HTTP di esempio della sezione 2 del corso: interroga un servizio
 * esterno per ottenere il nome di un prodotto dato il suo identificativo.
 */
public class ExternalServiceClient extends AbstractHttpClient {

	/**
	 * Richiede al servizio esterno il nome del prodotto con l'identificativo
	 * indicato.
	 *
	 * @param productId l'identificativo del prodotto da cercare
	 * @return un {@link Mono} con il nome del prodotto restituito dal servizio
	 */
	public Mono<String> getProductName(int productId) {
		return this.httpClient.get()
							  .uri("/demo01/product/"+ productId)
							  .responseContent()
							  .asString()
							  .next();
	}
}
