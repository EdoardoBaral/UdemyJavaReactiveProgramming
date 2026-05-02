package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class FluxInterval {
	
	public static void main(String[] args) {
		Flux.interval(Duration.ofMillis(500))
			.map(x -> Util.faker().name().name())
			.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
}
