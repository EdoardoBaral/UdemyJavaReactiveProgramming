package it.baral.sec13.client;

import it.baral.common.AbstractHttpClient;
import it.baral.sec13.RateLimiter;
import reactor.core.publisher.Mono;

public class ExternalServiceClient extends AbstractHttpClient {
	
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
