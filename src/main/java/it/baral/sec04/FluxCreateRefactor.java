package it.baral.sec04;

import it.baral.common.Util;
import it.baral.sec04.helper.NameGenerator;
import reactor.core.publisher.Flux;

public class FluxCreateRefactor {
	
	public static void main(String[] args) {
		NameGenerator nameGenerator = new NameGenerator();
		Flux<String> flux = Flux.create(nameGenerator);
		flux.subscribe(Util.subscriber("Sub1"));
		
		for(int i=0; i<10; i++) {
			nameGenerator.generate();
		}
	}
}
