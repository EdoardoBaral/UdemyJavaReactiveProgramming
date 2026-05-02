package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;

public class FluxDefer {
	
	public static void main(String[] args) {
		Flux.fromIterable(List.of(1, 2, 3, 4, 5))
			.subscribe(Util.subscriber());
		
		Flux.defer(() -> Flux.fromIterable(List.of(1, 2, 3, 4, 5)))
			.subscribe(Util.subscriber());
	}
}
