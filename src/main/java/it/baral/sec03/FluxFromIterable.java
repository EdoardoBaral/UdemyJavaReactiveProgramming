package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;

public class FluxFromIterable {
	
	public static void main(String[] args) {
		List<String> list = List.of("A", "B", "C", "D", "E");
		Flux.fromIterable(list)
			.subscribe(Util.subscriber());
		
		Integer[] array = {1, 2, 3, 4, 5};
		Flux.fromArray(array)
			.subscribe(Util.subscriber());
	}
}
