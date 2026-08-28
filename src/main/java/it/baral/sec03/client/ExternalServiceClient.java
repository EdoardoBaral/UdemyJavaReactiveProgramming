package it.baral.sec03.client;

import it.baral.common.AbstractHttpClient;
import reactor.core.publisher.Flux;

/**
 * Client HTTP di esempio della sezione 3 del corso: consuma due stream
 * continui esposti dal servizio esterno, uno di nomi e uno di variazioni di
 * prezzo di un titolo azionario.
 */
public class ExternalServiceClient extends AbstractHttpClient {

	/**
	 * Sottoscrive lo stream continuo di nomi esposto dal servizio esterno.
	 *
	 * @return un {@link Flux} che emette i nomi ricevuti dal servizio
	 */
	public Flux<String> getNames() {
		return this.httpClient.get()
							  .uri("/demo02/name/stream")
							  .responseContent()
							  .asString();
	}

	/**
	 * Sottoscrive lo stream continuo di variazioni di prezzo di un titolo
	 * azionario esposto dal servizio esterno, convertendo ogni valore
	 * ricevuto in un intero.
	 *
	 * @return un {@link Flux} che emette i prezzi ricevuti dal servizio
	 */
	public Flux<Integer> getPriceChanges() {
		return this.httpClient.get()
				   .uri("/demo02/stock/stream")
				   .responseContent()
				   .asString()
				   .map(Integer::parseInt);
	}
}
