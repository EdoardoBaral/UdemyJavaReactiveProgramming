package it.baral.sec04;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

public class FluxGenerateWithState {
	
	public static void main(String[] args) {
		Flux.generate(() -> 0, (counter, sink) -> {
			String country = Util.faker().country().name();
			sink.next(country);
			counter++;
			if(country.equalsIgnoreCase("canada") || counter == 10) {
				sink.complete();
			}
			return counter;
		}).subscribe(Util.subscriber());
	}
}
