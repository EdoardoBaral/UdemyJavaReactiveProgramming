package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Stream;

public class FluxFromStream {
	
	public static void main(String[] args) {
		List<Integer> list = List.of(1, 2, 3, 4, 5);
		Stream<Integer> stream = list.stream();
		
		Flux.fromStream(stream)
			.subscribe(Util.subscriber("Sub1"));
		
		//Non posso costruire un Flux a partire da uno stream già chiuso, devo crearne uno nuovo ogni volta
		Flux.fromStream(stream)
			.subscribe(Util.subscriber("Sub2"));
		
		//Soluzione ottimale, apro un nuovo stream ogni volta che voglio creare un Flux
		Flux.fromStream(list.stream())
			.subscribe(Util.subscriber("Sub3"));
		Flux.fromStream(list.stream())
			.subscribe(Util.subscriber("Sub4"));
	}
}
