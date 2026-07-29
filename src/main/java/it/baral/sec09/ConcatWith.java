package it.baral.sec09;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ConcatWith {
	
	private static final Logger log = LoggerFactory.getLogger(ConcatWith.class);
	
	public static void main(String[] args) {
//		concatWithValues();
//		concatWithPublisher();
//		multipleConcatWith();
		concat();
		
		Util.sleep(Duration.ofSeconds(3));
	}
	
	private static Flux<Integer> producer1() {
		return Flux.just(1, 2, 3)
				   .doOnSubscribe(s -> log.info("subscribing to producer1"))
				   .delayElements(Duration.ofMillis(10));
	}
	
	private static Flux<Integer> producer2() {
		return Flux.just(4, 5, 6)
				   .doOnSubscribe(s -> log.info("subscribing to producer2"))
				   .delayElements(Duration.ofMillis(10));
	}
	
	private static void concatWithValues() {
		producer1().concatWithValues(-1, 0)
				   .subscribe(Util.subscriber());
	}
	
	private static void concatWithPublisher() {
		producer1().concatWith(producer2())
				   .subscribe(Util.subscriber());
	}
	
	private static void multipleConcatWith() {
		producer1().concatWith(producer2())
				   .concatWithValues(1000)
				   .subscribe(Util.subscriber());
	}
	
	private static void concat() {
		Flux.concat(producer1(), producer2())
			.subscribe(Util.subscriber());
	}
}
