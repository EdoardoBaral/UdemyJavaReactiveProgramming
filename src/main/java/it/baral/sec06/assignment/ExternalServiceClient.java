package it.baral.sec06.assignment;

import it.baral.common.AbstractHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Client HTTP di esercitazione che espone lo stream di ordini di un servizio esterno
 * come publisher hot condiviso, usato per esercitarsi su {@code publish()}/{@code refCount}.
 */
public class ExternalServiceClient extends AbstractHttpClient {

	private static final Logger log = LoggerFactory.getLogger(ExternalServiceClient.class);

	private Flux<Order> orderFlux;

	/**
	 * Restituisce lo stream condiviso di ordini, creandolo alla prima chiamata e
	 * riutilizzandolo nelle successive, così che tutti i chiamanti condividano
	 * la stessa sottoscrizione hot al servizio esterno.
	 *
	 * @return il {@code Flux} condiviso degli ordini
	 */
	public Flux<Order> orderStream() {
		if(orderFlux == null) {
			this.orderFlux = this.getOrderStream();
		}

		return this.orderFlux;
	}

	/**
	 * Effettua una richiesta HTTP allo stream di ordini, ne effettua il parsing e
	 * lo rende hot con {@code publish().refCount(2)}, così che l'emissione inizi solo
	 * quando sono presenti almeno 2 sottoscrittori.
	 *
	 * @return un {@code Flux} di {@link Order} condiviso tra almeno 2 sottoscrittori
	 */
	public Flux<Order> getOrderStream() {
		return this.httpClient.get()
				   			  .uri("/demo04/orders/stream")
				   			  .responseContent()
				   			  .asString()
				   			  .map(this::parse)
				   			  .doOnNext(o -> log.info("{}", o))
				   .publish()
				   .refCount(2);
	}
	
	/**
	 * Effettua il parsing di un messaggio grezzo dello stream in un {@link Order},
	 * ignorando alcune informazioni presenti nel messaggio.
	 *
	 * @param message messaggio grezzo ricevuto dallo stream, nel formato separato da ":"
	 * @return l'{@link Order} corrispondente al messaggio
	 */
	private Order parse(String message) {
		String[] arr = message.split(":");
		return new Order(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3])); //Alcune informazioni del messaggio vengono ignorate
	}
}
