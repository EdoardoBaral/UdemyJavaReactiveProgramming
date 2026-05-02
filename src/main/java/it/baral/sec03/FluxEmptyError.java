package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

public class FluxEmptyError {
	
	public static void main(String[] args) {
		Flux.empty()
			.subscribe(Util.subscriber());
		
		Flux.error(new Exception("Oops"))
			.subscribe(Util.subscriber());
	}
}
