package it.baral.sec07;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class SubscribeOn {
	
	private static final Logger log = LoggerFactory.getLogger(SubscribeOn.class);
	
	public static void main(String[] args) {
		Flux<Integer> flux = Flux.create(sink -> {
								   for(int i=0; i<3; i++) {
									   log.info("generating: {}", i);
									   sink.next(i);
								   }
								   sink.complete();
							     })
								 .cast(Integer.class)
								 .doOnNext(v -> log.info("value: {}", v))
								 .doFirst(() -> log.info("first1"))
								 .subscribeOn(Schedulers.boundedElastic())
								 .doFirst(() -> log.info("first2"));
		
		Runnable runnable1 = () -> flux.subscribe(Util.subscriber("sub1"));
		Runnable runnable2 = () -> flux.subscribe(Util.subscriber("sub2"));
		
		Thread.ofPlatform().start(runnable1);
		Thread.ofPlatform().start(runnable2);
		
		Util.sleepSeconds(5);
	}
}
