package it.baral.sec09;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

public class StartWith {
	
	private static final Logger log = LoggerFactory.getLogger(StartWith.class);
	
	public static void main(String[] args) {
//		startWithElements();
//		startWithIterable();
//		startWithPublisher();
		multipleStartWith();
		
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
	
	private static void startWithElements() {
		producer1().startWith(-1, 0)
				   .take(3)
				   .subscribe(Util.subscriber());
	}
	
	private static void startWithIterable() {
		producer1().startWith(List.of(-1, 0))
				   .subscribe(Util.subscriber());
	}
	
	private static void startWithPublisher() {
		producer1().startWith(producer2())
				   .subscribe(Util.subscriber());
	}
	
	private static void multipleStartWith() {
		producer1().startWith(producer2())
				   .startWith(1000)
				   .subscribe(Util.subscriber());
	}
}
