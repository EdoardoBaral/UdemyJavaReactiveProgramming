package it.baral.sec02;

import it.baral.common.Util;
import reactor.core.publisher.Mono;

public class DemoDefaultSubscriber {
	
	public static void main(String[] args) {
		Mono<Integer> mono = Mono.just(1);
		mono.subscribe(Util.subscriber());
		mono.subscribe(Util.subscriber("Subscriber1"));
		mono.subscribe(Util.subscriber("Subscriber2"));
	}
}
