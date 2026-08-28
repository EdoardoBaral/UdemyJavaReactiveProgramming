package it.baral.sec11;

import it.baral.common.AbstractHttpClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClientResponse;

/**
 * Client HTTP reattivo verso il servizio esterno di esempio, usato per
 * dimostrare gli operatori {@code repeat} e {@code retry} in presenza di
 * risposte di errore client (400) o server (qualsiasi altro codice diverso
 * da 200), mappate rispettivamente su {@link ClientError} e {@link ServerError}.
 */
public class ExternalServiceClient extends AbstractHttpClient {

	/**
	 * Recupera il nome del prodotto identificato dall'id indicato.
	 *
	 * @param productId identificativo del prodotto da recuperare
	 * @return un {@link Mono} che emette il nome del prodotto, oppure fallisce con {@link ClientError}/{@link ServerError} in base allo status HTTP ricevuto
	 */
	public Mono<String> getProductName(int productId) {
		return get("/demo06/product/"+ productId);
	}

	/**
	 * Recupera il nome di un paese generato dal servizio esterno.
	 *
	 * @return un {@link Mono} che emette il nome del paese, oppure fallisce con {@link ClientError}/{@link ServerError} in base allo status HTTP ricevuto
	 */
	public Mono<String> getCountry() {
		return get("/demo06/country");
	}

	/**
	 * Esegue una richiesta HTTP GET verso il path indicato e converte la
	 * risposta in un {@link Mono} di stringa, mappando gli status HTTP di
	 * errore sulle eccezioni {@link ClientError}/{@link ServerError}.
	 *
	 * @param path il path relativo dell'endpoint da invocare
	 * @return un {@link Mono} contenente il corpo della risposta come stringa
	 */
	private Mono<String> get(String path) {
		return this.httpClient.get()
				   			  .uri(path)
				   			  .response(this::toResponse)
				   			  .next();
	}

	/**
	 * Converte lo status HTTP della risposta nel relativo {@link Flux} di
	 * risultato: il corpo come stringa in caso di successo (200), oppure
	 * un errore ({@link ClientError} per il 400, {@link ServerError} per
	 * ogni altro codice).
	 *
	 * @param response la risposta HTTP ricevuta, da cui leggere lo status code
	 * @param byteBufFlux il corpo della risposta, letto come flusso di byte
	 * @return un {@link Flux} che emette il corpo come stringa, oppure fallisce con {@link ClientError}/{@link ServerError}
	 */
	private Flux<String> toResponse(HttpClientResponse response, ByteBufFlux byteBufFlux) {
		return switch(response.status().code()) {
			case 200 -> byteBufFlux.asString();
			case 400 -> Flux.error(new ClientError());
			default -> Flux.error(new ServerError());
		};
	}
}
