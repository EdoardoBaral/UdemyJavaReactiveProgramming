package it.baral.sec02;

import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class MonoSubscribe {
	
	private static final Logger log = LoggerFactory.getLogger(MonoSubscribe.class);
	
	public static void main(String[] args) throws Exception {
		Mono<Integer> mono = Mono.just(1);
		mono.subscribe(i -> log.info("Received: {}", i),
					   error -> log.error("Error: ", error),
					   () -> log.info("Completed"));
		System.out.println();
		
		Mono<Integer> mono2 = Mono.just(1);
		mono2.subscribe(i -> log.info("Received: {}", i),
					    error -> log.error("Error: ", error),
					    () -> log.info("Completed"),
					    Subscription::cancel);
		
		Mono<Integer> mono3 = Mono.just(1);
		mono3.subscribe(i -> log.info("Received: {}", i),
						error -> log.error("Error: ", error),
						() -> log.info("Completed"),
						subscription -> subscription.request(1));
		System.out.println();
		
		Mono<String> mono4 = Mono.just(1)
								 .map(i -> i + "a");
		mono4.subscribe(i -> log.info("Received: {}", i),
						error -> log.error("Error: ", error),
						() -> log.info("Completed"),
						subscription -> subscription.request(1));
		System.out.println();
		
		Mono<Integer> mono5 = Mono.just(1)
								  .map(i -> i/0);
		mono5.subscribe(i -> log.info("Received: {}", i),
						error -> log.error("Error: ", error),
						() -> log.info("Completed"),
						subscription -> subscription.request(1));
		System.out.println();
	}
}
