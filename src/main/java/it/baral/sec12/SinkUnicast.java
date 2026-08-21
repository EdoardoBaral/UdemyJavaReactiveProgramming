package it.baral.sec12;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SinkUnicast {
	
	private static final Logger log = LoggerFactory.getLogger(SinkUnicast.class);
	
	public static void main(String[] args) {
//		demoSinkUnicast();
		demoSinkUnicastMultipleSubscribers();
	}
	
	private static void demoSinkUnicast() {
		Sinks.Many<String> sink = Sinks.many()
									   .unicast()
									   .onBackpressureBuffer();
		Flux<String> flux = sink.asFlux();
		
		sink.tryEmitNext("hello");
		sink.tryEmitNext("how");
		sink.tryEmitNext("are");
		sink.tryEmitNext("you");
		sink.tryEmitNext("?");
		
		flux.subscribe(Util.subscriber());
	}
	
	private static void demoSinkUnicastMultipleSubscribers() {
		Sinks.Many<String> sink = Sinks.many()
									   .unicast()
									   .onBackpressureBuffer();
		Flux<String> flux = sink.asFlux();
		
		sink.tryEmitNext("hello");
		sink.tryEmitNext("how");
		sink.tryEmitNext("are");
		sink.tryEmitNext("you");
		sink.tryEmitNext("?");
		
		flux.subscribe(Util.subscriber("sam"));
		flux.subscribe(Util.subscriber("mike"));
	}
}
