package it.baral.sec13;

import it.baral.common.Util;
import it.baral.sec13.client.ExternalServiceClient;
import reactor.util.context.Context;

public class RateLimiterDemo {
	
	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();
		for(int i=1; i<=20; i++) {
			client.getBook()
				  .contextWrite(Context.of("user", "sam"))
				  .subscribe(Util.subscriber());
			Util.sleepSeconds(1);
		}
		
		Util.sleepSeconds(15);
	}
}
