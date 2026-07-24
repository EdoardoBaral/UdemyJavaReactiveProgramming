package it.baral.sec05;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class ErrorHandling {
	
	private static final Logger log = LoggerFactory.getLogger(ErrorHandling.class);
	
	public static void main(String[] args) {
//		onErrorReturnSimple();
//		onErrorReturnWithException();
//		onErrorResume();
//		onErrorResumeWithException();
//		onErrorComplete();
		onErrorContinue();
	}
	
	private static void onErrorReturnSimple() {
		Flux.range(1, 10)
			.map(i -> i == 5 ? 5/0 : i)
			.onErrorReturn(-1)
			.subscribe(Util.subscriber());
	}
	
	private static void onErrorReturnWithException() {
		Flux.range(1, 10)
			.map(i -> i == 5 ? 5/0 : i)
			.onErrorReturn(ArithmeticException.class, -1)
			.subscribe(Util.subscriber());
	}
	
	private static void onErrorResume() {
		Flux.range(1, 10)
			.map(i -> i == 5 ? 5/0 : i)
			.onErrorResume(ex -> fallback())
			.subscribe(Util.subscriber());
	}
	
	private static void onErrorResumeWithException() {
		Flux.range(1, 10)
			.map(i -> i == 5 ? 5/0 : i)
			.onErrorResume(ArithmeticException.class, ex -> fallback())
			.subscribe(Util.subscriber());
	}
	
	private static void onErrorComplete() {
		Flux.range(1, 10)
			.map(i -> i == 5 ? 5/0 : i)
			.onErrorComplete()
			.subscribe(Util.subscriber());
	}
	
	private static void onErrorContinue() {
		Flux.range(1, 10)
			.map(i -> i == 5 ? 5/0 : i)
			.onErrorContinue((ex, item) -> log.error("--> {}", item, ex))
			.subscribe(Util.subscriber());
	}
	
	private static Flux<Integer> fallback() {
		return Flux.range(1, 5)
				   .map(i -> Util.faker().random().nextInt(1, 10));
	}
}
