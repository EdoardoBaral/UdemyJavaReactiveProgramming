package it.baral.sec09;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Merge {
	
	private static final Logger log = LoggerFactory.getLogger(Merge.class);
	
	public static void main(String[] args) {
//		merge();
		mergeWith();
		
		Util.sleepSeconds(5);
	}
	
	private static Flux<Integer> producer1() {
		return Flux.just(1, 2, 3)
				   .transform(Util.fluxLogger("producer1"))
				   .delayElements(Duration.ofMillis(10));
	}
	
	private static Flux<Integer> producer2() {
		return Flux.just(4, 5, 6)
				   .transform(Util.fluxLogger("producer2"))
				   .delayElements(Duration.ofMillis(10));
	}
	
	private static Flux<Integer> producer3() {
		return Flux.just(7, 8, 9)
				   .transform(Util.fluxLogger("producer3"))
				   .delayElements(Duration.ofMillis(10));
	}
	
	private static void merge() {
		Flux.merge(producer1(), producer2(), producer3())
			.take(2)
			.subscribe(Util.subscriber());
	}
	
	private static void mergeWith() {
		producer1().mergeWith(producer2())
				   .mergeWith(producer3())
				   .take(2)
				   .subscribe(Util.subscriber());
	}
}
