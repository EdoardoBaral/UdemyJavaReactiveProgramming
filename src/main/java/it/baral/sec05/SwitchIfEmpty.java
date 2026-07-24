package it.baral.sec05;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

public class SwitchIfEmpty {
	
	public static void main(String[] args) {
//		normalFlux();
		switchIfEmpty();
	}
	
	private static void normalFlux() {
		Flux.range(1, 10)
			.filter(i -> i < 6)
			.switchIfEmpty(fallback())
			.subscribe(Util.subscriber());
	}
	
	private static void switchIfEmpty() {
		Flux.range(1, 10)
			.filter(i -> i > 10)
			.switchIfEmpty(fallback())
			.subscribe(Util.subscriber());
	}
	
	private static Flux<Integer> fallback() {
		return Flux.just(-1);
	}
}
