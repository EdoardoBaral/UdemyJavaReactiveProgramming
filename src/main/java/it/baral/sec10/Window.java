package it.baral.sec10;

import it.baral.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class Window {
	
	public static void main(String[] args) {
		eventStream().window(5)
					 .flatMap(Window::processEvents)
					 .subscribe();
		
		Util.sleepSeconds(60);
	}
	
	private static Flux<String> eventStream() {
		return Flux.interval(Duration.ofMillis(200))
				   .map(i -> "event "+ (i+1));
	}
	
	private static Mono<Void> processEvents(Flux<String> flux) {
		return flux.doOnNext(e -> System.out.print("*"))
				   .doOnComplete(System.out::println)
				   .then();
	}
}
