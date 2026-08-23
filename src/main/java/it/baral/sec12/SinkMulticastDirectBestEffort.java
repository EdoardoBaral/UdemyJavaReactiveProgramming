package it.baral.sec12;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

public class SinkMulticastDirectBestEffort {

	private static final Logger log = LoggerFactory.getLogger(SinkMulticastDirectBestEffort.class);
	
	public static void main(String[] args) {
//		demoFailure();
		demoSuccess();
		Util.sleepSeconds(30);
	}
	
	private static void demoFailure() {
		System.setProperty("reactor.bufferSize.small", "16");
		Sinks.Many<Integer> sink = Sinks.many()
									   .multicast()
									   .onBackpressureBuffer();
		Flux<Integer> flux = sink.asFlux();
		
		flux.subscribe(Util.subscriber("sam"));
		flux.delayElements(Duration.ofMillis(200))
			.subscribe(Util.subscriber("mike"));
		
		for(int i=1; i<=100; i++) {
			Sinks.EmitResult result = sink.tryEmitNext(i);
			log.info("item: {} - result: {}", i, result);
		}
	}
	
	private static void demoSuccess() {
		System.setProperty("reactor.bufferSize.small", "16");
		Sinks.Many<Integer> sink = Sinks.many()
									    .multicast()
									    .directBestEffort();
		Flux<Integer> flux = sink.asFlux();
		
		flux.subscribe(Util.subscriber("sam"));
		flux.onBackpressureBuffer()
			.delayElements(Duration.ofMillis(200))
			.subscribe(Util.subscriber("mike"));
		
		for(int i=1; i<=100; i++) {
			Sinks.EmitResult result = sink.tryEmitNext(i);
			log.info("item: {} - result: {}", i, result);
		}
	}
}
