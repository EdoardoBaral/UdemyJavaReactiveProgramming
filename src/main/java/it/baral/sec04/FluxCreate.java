package it.baral.sec04;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

public class FluxCreate {
	
	public static void main(String[] args) {
		Flux.create(fluxSink -> {
				fluxSink.next(1);
				fluxSink.next(2);
				fluxSink.complete();
			})
			.subscribe(Util.subscriber("Sub1"));
		
		Flux.create(fluxSink -> {
				for(int i=0; i<10; i++) {
					fluxSink.next(Util.faker().country().name());
				}
				fluxSink.complete();
			})
			.subscribe(Util.subscriber("Sub2"));
		
		Flux.create(fluxSink -> {
				String countryName = "";
				do {
					countryName = Util.faker().country().name();
					fluxSink.next(countryName);
				} while(!"Canada".equals(countryName));
				fluxSink.complete();
			})
			.subscribe(Util.subscriber("Sub3"));
	}
}
