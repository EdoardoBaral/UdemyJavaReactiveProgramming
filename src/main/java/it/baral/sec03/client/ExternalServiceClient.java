package it.baral.sec03.client;

import it.baral.common.AbstractHttpClient;
import reactor.core.publisher.Flux;

public class ExternalServiceClient extends AbstractHttpClient {

	public Flux<String> getNames() {
		return this.httpClient.get()
							  .uri("/demo02/name/stream")
							  .responseContent()
							  .asString();
	}
}
