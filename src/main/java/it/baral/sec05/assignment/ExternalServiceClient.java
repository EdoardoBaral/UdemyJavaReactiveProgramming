package it.baral.sec05.assignment;

import it.baral.common.AbstractHttpClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Client HTTP di esercitazione che recupera il nome di un prodotto da un servizio
 * primario, con fallback in caso di timeout o di risposta vuota, usato per esercitarsi
 * sugli operatori {@code timeout} e {@code switchIfEmpty}.
 */
public class ExternalServiceClient extends AbstractHttpClient {

	/**
	 * Recupera il nome del prodotto dal servizio primario, ricadendo sul servizio
	 * di timeout-fallback se la risposta impiega più di 2 secondi, e sul servizio
	 * di empty-fallback se la risposta primaria (o quella di timeout) è vuota.
	 *
	 * @param productId identificativo del prodotto da recuperare
	 * @return un {@code Mono} che emette il nome del prodotto
	 */
	public Mono<String> getProductName(int productId) {
		String defaultPath = "/demo03/product/"+ productId;
		String timeoutPath = "/demo03/timeout-fallback/product/"+ productId;
		String emptyPath = "/demo03/empty-fallback/product/"+ productId;

		return getProductName(defaultPath)
				.timeout(Duration.ofSeconds(2), getProductName(timeoutPath))
				.switchIfEmpty(getProductName(emptyPath));
	}

	/**
	 * Effettua una richiesta HTTP GET al percorso indicato e ne restituisce il corpo
	 * come stringa.
	 *
	 * @param path percorso della richiesta HTTP
	 * @return un {@code Mono} che emette il corpo della risposta come stringa
	 */
	public Mono<String> getProductName(String path) {
		return this.httpClient.get()
				   			  .uri(path)
				   			  .responseContent()
				   			  .asString()
				   			  .next();
	}
}
