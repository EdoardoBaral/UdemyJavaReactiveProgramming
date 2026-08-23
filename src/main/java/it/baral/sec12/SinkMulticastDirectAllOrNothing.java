package it.baral.sec12;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

public class SinkMulticastDirectAllOrNothing {
	
	private static final Logger log = LoggerFactory.getLogger(SinkMulticastDirectAllOrNothing.class);
	
	public static void main(String[] args) {
		demoSuccess();
		Util.sleepSeconds(30);
	}
	
	private static void demoSuccess() {
		System.setProperty("reactor.bufferSize.small", "16");
		Sinks.Many<Integer> sink = Sinks.many()
									    .multicast()
									    .directAllOrNothing();
		Flux<Integer> flux = sink.asFlux();
		
		flux.subscribe(Util.subscriber("sam"));
		flux.delayElements(Duration.ofMillis(200))
			.subscribe(Util.subscriber("mike"));
		
		for(int i=1; i<=100; i++) {
			Sinks.EmitResult result = sink.tryEmitNext(i);
			log.info("item: {} - result: {}", i, result);
		}
	}
}
