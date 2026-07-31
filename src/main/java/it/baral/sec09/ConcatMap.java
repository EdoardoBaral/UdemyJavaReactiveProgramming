package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.assignment.ExternalServiceClient;
import reactor.core.publisher.Flux;

public class ConcatMap {
	
	public static void main(String[] args) {
		ExternalServiceClient serviceClient = new ExternalServiceClient();
		
		Flux.range(1, 10)
			.concatMap(serviceClient::getProduct)
			.subscribe(Util.subscriber());
		
		Util.sleepSeconds(10);
	}
}
