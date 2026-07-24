package it.baral.sec05;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class Timeout {
	
	private static final Logger log = LoggerFactory.getLogger(Timeout.class);
	
	public static void main(String[] args) {
//		noTimeoutCase();
//		timeoutCase();
//		timeoutWithFallbackCase();
		multipleTimeoutsCase();
	}
	
	private static void noTimeoutCase() {
		getProduct().timeout(Duration.ofSeconds(10))
					.onErrorReturn("fallback")
					.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
	
	private static void timeoutCase() {
		getProduct().timeout(Duration.ofSeconds(1))
					.onErrorReturn("fallback")
					.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
	
	private static void timeoutWithFallbackCase() {
		getProduct().timeout(Duration.ofSeconds(2), getProductFallback())
					.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
	
	private static void multipleTimeoutsCase() {
		Mono<String> mono = getProduct().timeout(Duration.ofSeconds(1), getProductFallback());
		
		mono.timeout(Duration.ofMillis(200))
			.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
	
	private static Mono<String> getProduct() {
		return Mono.just("Primary service - "+ Util.faker().commerce().productName())
				   .delayElement(Duration.ofSeconds(3));
	}
	
	private static Mono<String> getProductFallback() {
		return Mono.just("Fallback service - " + Util.faker().commerce().productName())
				   .delayElement(Duration.ofSeconds(2));
	}
}
