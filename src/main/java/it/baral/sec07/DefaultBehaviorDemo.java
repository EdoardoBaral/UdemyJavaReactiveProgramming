package it.baral.sec07;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class DefaultBehaviorDemo {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultBehaviorDemo.class);
	
	public static void main(String[] args) {
		Flux<Integer> flux = Flux.create(sink -> {
								 	for(int i=0; i<3; i++) {
								 		log.info("generating: {}", i);
								 		sink.next(i);
								 	}
								 	sink.complete();
								 })
								 .cast(Integer.class)
								 .doOnNext(v -> log.info("value: {}", v));
		
		Runnable runnable1 = () -> flux.subscribe(Util.subscriber("sub1"));
		Runnable runnable2 = () -> flux.subscribe(Util.subscriber("sub2"));
		
		Thread.ofPlatform().start(runnable1);
		Thread.ofPlatform().start(runnable2);
	}
}
