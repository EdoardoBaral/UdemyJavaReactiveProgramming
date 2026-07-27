package it.baral.sec06.assignment;

import it.baral.common.AbstractHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ExternalServiceClient extends AbstractHttpClient {
	
	private static final Logger log = LoggerFactory.getLogger(ExternalServiceClient.class);

	private Flux<Order> orderFlux;
	
	public Flux<Order> orderStream() {
		if(orderFlux == null) {
			this.orderFlux = this.getOrderStream();
		}
		
		return this.orderFlux;
	}
	
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
	
	private Order parse(String message) {
		String[] arr = message.split(":");
		return new Order(arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3])); //Alcune informazioni del messaggio vengono ignorate
	}
}
