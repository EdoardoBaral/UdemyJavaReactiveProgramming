package it.baral.sec12;

import it.baral.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinkMulticastReplay {
	
	public static void main(String[] args) {
		demoSinkMulticastReplay();
		Util.sleepSeconds(10);
	}
	
	private static void demoSinkMulticastReplay() {
		Sinks.Many<String> sink = Sinks.many()
									   .replay()
									   .all();
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
}
