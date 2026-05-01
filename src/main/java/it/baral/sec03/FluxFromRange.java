package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

public class FluxFromRange {
	
	public static void main(String[] args) {
		Flux.range(1, 10)
			.subscribe(Util.subscriber());
		
		Flux.range(1, 10)
			.map(x -> Util.faker().name().name())
			.subscribe(Util.subscriber());
	}
}
