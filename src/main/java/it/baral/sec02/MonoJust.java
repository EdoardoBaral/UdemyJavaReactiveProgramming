package it.baral.sec02;

import it.baral.sec01.subscriber.SubscriberImpl;
import reactor.core.publisher.Mono;

public class MonoJust {
	
	public static void main(String[] args) {
		Mono<String> mono = Mono.just("Edoardo");
		SubscriberImpl subscriber = new SubscriberImpl();
		mono.subscribe(subscriber);
		
		subscriber.getSubscription().request(3);
		subscriber.getSubscription().request(3);
		subscriber.getSubscription().cancel();
	}
}
