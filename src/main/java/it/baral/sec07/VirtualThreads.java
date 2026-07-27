package it.baral.sec07;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class VirtualThreads {
	
	private static final Logger log = LoggerFactory.getLogger(VirtualThreads.class);
	
	public static void main(String[] args) {
		System.setProperty("reactor.schedulers.defaultBoundedElasticOnVirtualThreads", "true");
		Flux<Integer> flux = Flux.create(sink -> {
								 	for(int i=0; i<3; i++) {
								 		log.info("generating: {}", i);
								 		sink.next(i);
								 	}
								 	sink.complete();
								 })
								 .cast(Integer.class)
								 .doOnNext(v -> log.info("value: {}", v))
								 .doFirst(() -> log.info("first1-{}", Thread.currentThread().isVirtual()))
								 .subscribeOn(Schedulers.boundedElastic())
								 .doFirst(() -> log.info("first2"));
		
		Runnable runnable1 = () -> flux.subscribe(Util.subscriber("sub1"));
		Thread.ofPlatform().start(runnable1);
		
		Util.sleepSeconds(5);
	}
}
