package it.baral.sec06;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

public class ColdPublisher {
	
	private static final Logger log = LoggerFactory.getLogger(ColdPublisher.class);
	
	public static void main(String[] args) {
		AtomicInteger atomicInteger = new AtomicInteger();
		Flux<Integer> flux = Flux.create(sink -> {
								 	log.info("invoked");
								 	for(int i=0; i<3; i++) {
								 		sink.next(atomicInteger.incrementAndGet());
								 	}
								 	sink.complete();
								 });
		
		flux.subscribe(Util.subscriber("sub1"));
		flux.subscribe(Util.subscriber("sub2"));
	}
}
