package it.baral.sec09.assignment;

import it.baral.common.AbstractHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Client HTTP di esempio usato negli esercizi della sezione sec09 per dimostrare
 * la combinazione di più chiamate {@link Mono} (nome, recensione e prezzo di un prodotto)
 * tramite {@code Mono.zip}, e per gli esercizi su {@code flatMap}/{@code concatMap}.
 */
public class ExternalServiceClient extends AbstractHttpClient {

	/**
	 * Recupera un {@link Product} completo componendo in parallelo nome, recensione e prezzo
	 * ottenuti da tre chiamate HTTP separate.
	 *
	 * @param productId identificativo del prodotto da recuperare
	 * @return un {@code Mono} che emette il {@link Product} composto dai tre valori recuperati
	 */
	public Mono<Product> getProduct(int productId) {
		return Mono.zip(getProductName(productId), getReview(productId), getPrice(productId))
				   .map(t -> new Product(t.getT1(), t.getT2(), t.getT3()));
	}

	/**
	 * Recupera il nome del prodotto dato il suo identificativo.
	 *
	 * @param productId identificativo del prodotto
	 * @return un {@code Mono} che emette il nome del prodotto
	 */
	private Mono<String> getProductName(int productId) {
		return get("/demo05/product/"+ productId);
	}

	/**
	 * Recupera la recensione del prodotto dato il suo identificativo.
	 *
	 * @param productId identificativo del prodotto
	 * @return un {@code Mono} che emette la recensione del prodotto
	 */
	private Mono<String> getReview(int productId) {
		return get("/demo05/review/"+ productId);
	}

	/**
	 * Recupera il prezzo del prodotto dato il suo identificativo.
	 *
	 * @param productId identificativo del prodotto
	 * @return un {@code Mono} che emette il prezzo del prodotto
	 */
	private Mono<String> getPrice(int productId) {
		return get("/demo05/price/"+ productId);
	}

	/**
	 * Esegue una richiesta HTTP GET sul path indicato e ne restituisce il corpo come stringa.
	 *
	 * @param path percorso relativo dell'endpoint da invocare
	 * @return un {@code Mono} che emette il corpo della risposta come stringa
	 */
	private Mono<String> get(String path) {
		return this.httpClient.get()
				   .uri(path)
				   .responseContent()
				   .asString()
				   .next();
	}
}
