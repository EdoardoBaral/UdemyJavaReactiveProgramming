package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

public class MultipleSubscribers {
	
	public static void main(String[] args) {
		Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);
		flux.subscribe(Util.subscriber("Sub1"));
		
		flux.filter(e -> e > 7)
			.subscribe(Util.subscriber("Sub2"));
		
		flux.filter(e -> e % 2 == 0)
			.map(e -> e +"a")
			.subscribe(Util.subscriber("Sub3"));
	}
}
