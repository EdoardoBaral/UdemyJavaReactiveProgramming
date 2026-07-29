package it.baral.sec09;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class ConcatError {
	
	private static final Logger log = LoggerFactory.getLogger(ConcatError.class);
	
	public static void main(String[] args) {
//		concatWith();
		concatDelayError();
		
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
	
	private static Flux<Integer> producer3() {
		return Flux.error(new RuntimeException("ooops"));
	}
	
	private static void concatWith() {
		producer1().concatWith(producer3())
				   .concatWith(producer2())
				   .subscribe(Util.subscriber());
	}
	
	private static void concatDelayError() {
		Flux.concatDelayError(producer1(), producer3(), producer2())
			.subscribe(Util.subscriber());
	}
}
