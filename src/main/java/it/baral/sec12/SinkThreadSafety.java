package it.baral.sec12;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SinkThreadSafety {
	
	private static final Logger log = LoggerFactory.getLogger(SinkThreadSafety.class);
	
	public static void main(String[] args) {
//		demoSinkThreadUnsafe();
		demoSinkThreadSafe();
	}
	
	private static void demoSinkThreadUnsafe() {
		Sinks.Many<Integer> sink = Sinks.many()
									    .unicast()
									    .onBackpressureBuffer();
		Flux<Integer> flux = sink.asFlux();
		
		List<Integer> list = new ArrayList<>();
		flux.subscribe(list::add);
		
		for(int i=0; i<1000; i++) {
			int j = i;
			CompletableFuture.runAsync(() -> sink.tryEmitNext(j));
		}
		
		Util.sleepSeconds(5);
		log.info("list size: {}", list.size());
	}
	
	private static void demoSinkThreadSafe() {
		Sinks.Many<Integer> sink = Sinks.many()
									    .unicast()
									    .onBackpressureBuffer();
		Flux<Integer> flux = sink.asFlux();
		
		List<Integer> list = new ArrayList<>();
		flux.subscribe(list::add);
		
		for(int i=0; i<1000; i++) {
			int j = i;
			CompletableFuture.runAsync(() -> sink.emitNext(j, (signal, emitResult) -> Sinks.EmitResult.FAIL_NON_SERIALIZED.equals(emitResult)));
		}
		
		Util.sleepSeconds(5);
		log.info("list size: {}", list.size());
	}
}
