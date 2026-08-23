package it.baral.sec12;

import it.baral.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinkMulticast {
	
	public static void main(String[] args) {
//		demoSinkMulticast();
		demoSinkMulticastWithWarmup();
	}
	
	private static void demoSinkMulticast() {
		Sinks.Many<String> sink = Sinks.many()
									   .multicast()
									   .onBackpressureBuffer();
		Flux<String> flux = sink.asFlux();
		
		flux.subscribe(Util.subscriber("sam"));
		flux.subscribe(Util.subscriber("mike"));
		
		sink.tryEmitNext("hello");
		sink.tryEmitNext("how");
		sink.tryEmitNext("are");
		sink.tryEmitNext("you");
		sink.tryEmitNext("?");
		
		Util.sleepSeconds(2);
		
		flux.subscribe(Util.subscriber("john"));
		sink.tryEmitNext("goodbye");
	}
	
	private static void demoSinkMulticastWithWarmup() {
		Sinks.Many<String> sink = Sinks.many()
									   .multicast()
									   .onBackpressureBuffer();
		Flux<String> flux = sink.asFlux();
		
		sink.tryEmitNext("hello");
		sink.tryEmitNext("how");
		sink.tryEmitNext("are");
		sink.tryEmitNext("you");
		sink.tryEmitNext("?");
		
		Util.sleepSeconds(2);
		
		flux.subscribe(Util.subscriber("sam"));
		flux.subscribe(Util.subscriber("mike"));
		flux.subscribe(Util.subscriber("john"));
		
		sink.tryEmitNext("goodbye");
	}
}
