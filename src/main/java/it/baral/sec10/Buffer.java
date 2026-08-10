package it.baral.sec10;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Buffer {
	
	public static void main(String[] args) {
//		bufferDemo();
//		bufferWithDimensionDemo();
//		bufferWithDurationDemo();
//		bufferWithDimensionAndInfiniteFluxDemo();
		bufferTimeoutDemo();
		
		Util.sleepSeconds(5);
	}
	
	private static void bufferDemo() {
		eventStream().buffer()
					 .subscribe(Util.subscriber());
	}
	
	private static void bufferWithDimensionDemo() {
		eventStream().buffer(3)
					 .subscribe(Util.subscriber());
	}
	
	private static void bufferWithDurationDemo() {
		eventStream().buffer(Duration.ofMillis(500))
					 .subscribe(Util.subscriber());
	}
	
	private static void bufferWithDimensionAndInfiniteFluxDemo() {
		eventStreamInfinite().buffer(3)
							 .subscribe(Util.subscriber());
	}
	
	private static void bufferTimeoutDemo() {
		eventStreamInfinite().bufferTimeout(3, Duration.ofSeconds(1))
			.subscribe(Util.subscriber());
	}
	
	private static Flux<String> eventStream() {
		return Flux.interval(Duration.ofMillis(200))
				   .take(10)
				   .map(i -> "event "+ (i+1));
	}
	
	private static Flux<String> eventStreamInfinite() {
		return Flux.interval(Duration.ofMillis(200))
				   .take(10)
				   .concatWith(Flux.never())
				   .map(i -> "event "+ (i+1));
	}
}
