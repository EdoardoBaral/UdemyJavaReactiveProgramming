package it.baral.sec04;

import it.baral.common.Util;
import it.baral.sec01.subscriber.SubscriberImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class FluxCreateDownstreamDemand {
	
	private static final Logger log = LoggerFactory.getLogger(FluxCreateDownstreamDemand.class);
	
	public static void main(String[] args) {
		SubscriberImpl subscriber = new SubscriberImpl();
		Flux.<String>create(fluxSink -> {
				for(int i=0; i<10; i++) {
					String name = Util.faker().name().fullName();
					log.info("Generated: {}", name);
					fluxSink.next(name);
				}
				fluxSink.complete();
			})
			.subscribe(subscriber);
		
		Util.sleepSeconds(2);
		subscriber.getSubscription().request(2);
		Util.sleepSeconds(2);
		subscriber.getSubscription().request(2);
		subscriber.getSubscription().cancel();
		
		Util.sleepSeconds(2);
		subscriber.getSubscription().request(2);
	}
}
