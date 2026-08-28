package it.baral.sec13.client;

import it.baral.common.AbstractHttpClient;
import it.baral.sec13.RateLimiter;
import reactor.core.publisher.Mono;

/**
 * Client HTTP di esempio verso un servizio esterno, che dimostra l'applicazione del
 * {@link RateLimiter} tramite il Context reattivo prima di effettuare la chiamata remota.
 */
public class ExternalServiceClient extends AbstractHttpClient {

	/**
	 * Recupera un libro dal servizio esterno, applicando prima il controllo del rate limit
	 * in base alla categoria dell'utente presente nel Context (impostata da
	 * {@link UserService#userCategoryContext()}). Se il limite è superato, il Mono restituito
	 * fallisce senza effettuare la chiamata HTTP.
	 *
	 * @return un {@link Mono} che emette il corpo della risposta HTTP come stringa,
	 *         oppure fallisce con errore se il rate limit è stato superato
	 */
	public Mono<String> getBook() {
		return this.httpClient.get()
							  .uri("/demo07/book")
							  .responseContent()
							  .asString()
				   			  .startWith(RateLimiter.limitCalls())
				   			  .contextWrite(UserService.userCategoryContext())
							  .next();
	}
}
