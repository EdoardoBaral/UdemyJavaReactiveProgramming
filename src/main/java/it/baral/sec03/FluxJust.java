package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

public class FluxJust {
	
	public static void main(String[] args) {
		Flux.just(1, 2, 3, 4, 5)
			.subscribe(Util.subscriber());
	}
}
