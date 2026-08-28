package it.baral.sec07.client;

import it.baral.common.AbstractHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Client HTTP di esempio usato negli esercizi sulla sezione 07 (scheduling) per
 * dimostrare la correzione del blocco dell'event loop tramite {@code publishOn}.
 */
public class ExternalServiceClient extends AbstractHttpClient {

	private static final Logger log = LoggerFactory.getLogger(ExternalServiceClient.class);

	/**
	 * Recupera il nome del prodotto dato il suo identificativo, spostando l'emissione
	 * del risultato sullo scheduler {@code boundedElastic} tramite {@code publishOn}
	 * per non bloccare i thread dell'event loop Netty.
	 *
	 * @param productId l'identificativo del prodotto da richiedere
	 * @return un {@link Mono} che emette il nome del prodotto restituito dal servizio
	 */
	public Mono<String> getProductName(int productId) {
		return this.httpClient.get()
							  .uri("/demo01/product/"+ productId)
							  .responseContent()
							  .asString()
				   			  .doOnNext(m -> log.info("next: {}", m))
							  .next()
							  .publishOn(Schedulers.boundedElastic());
	}
}
