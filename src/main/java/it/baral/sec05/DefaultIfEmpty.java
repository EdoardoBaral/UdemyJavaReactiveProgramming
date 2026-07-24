package it.baral.sec05;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

public class DefaultIfEmpty {
	
	public static void main(String[] args) {
		emptyFlux();
//		normalFlux();
	}
	
	private static void emptyFlux() {
		Flux.range(1, 10)
			.filter(i -> i > 10)
			.defaultIfEmpty(-1)
			.subscribe(Util.subscriber());
	}
	
	private static void normalFlux() {
		Flux.range(1, 10)
			.defaultIfEmpty(-1)
			.subscribe(Util.subscriber());
	}
}
