package it.baral.sec09;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Zip {
	
	private record Car(String body, String engine, String tires) {}
	
	public static void main(String[] args) {
		zip();
		
		Util.sleepSeconds(5);
	}

	private static Flux<String> body() {
		return Flux.range(1, 5)
				   .map(i -> "body-"+ i)
				   .delayElements(Duration.ofMillis(100));
	}
	
	private static Flux<String> engine() {
		return Flux.range(1, 3)
				   .map(i -> "engine-"+ i)
				   .delayElements(Duration.ofMillis(200));
	}
	
	private static Flux<String> tires() {
		return Flux.range(1, 10)
				   .map(i -> "tires-"+ i)
				   .delayElements(Duration.ofMillis(75));
	}
	
	private static void zip() {
		Flux.zip(body(), engine(), tires())
			.map(i -> new Car(i.getT1(), i.getT2(), i.getT3()))
			.subscribe(Util.subscriber());
	}
}
