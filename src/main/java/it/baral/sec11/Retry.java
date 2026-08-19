package it.baral.sec11;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Retry {
	
	private static final Logger log = LoggerFactory.getLogger(Retry.class);
	
	public static void main(String[] args) {
//		demoWithoutRetry();
//		demoWithRetry();
//		demoWithRetryWhen();
		demoWithRetryWhenFilter();
		
		Util.sleepSeconds(10);
	}
	
	private static void demoWithoutRetry() {
		getCountryName().subscribe(Util.subscriber());
	}
	
	private static void demoWithRetry() {
		getCountryName().retry(5)
						.subscribe(Util.subscriber());
	}
	
	private static void demoWithRetryWhen() {
		getCountryName().retryWhen(reactor.util.retry.Retry.fixedDelay(2, Duration.ofSeconds(1))
									                       .doBeforeRetry(i -> log.info("retrying")))
						.subscribe(Util.subscriber());
	}
	
	private static void demoWithRetryWhenFilter() {
		getCountryName().retryWhen(reactor.util.retry.Retry.fixedDelay(2, Duration.ofSeconds(1))
														   .filter(err -> err instanceof RuntimeException))
			.subscribe(Util.subscriber());
	}
	
	private static Mono<String> getCountryName() {
		AtomicInteger x = new AtomicInteger(0);
		return Mono.fromSupplier(() -> {
					   if(x.incrementAndGet() < 3) {
					   		throw new RuntimeException("oops");
					   }
					   return Util.faker().country().name();
				   })
				   .doOnError(err -> log.error("ERROR: {}", err.getMessage()))
				   .doOnSubscribe(s -> log.info("subscribed"));
	}
}
