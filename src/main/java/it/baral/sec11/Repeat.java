package it.baral.sec11;

import it.baral.common.Util;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class Repeat {
	
	public static void main(String[] args) {
//		demoRepeatNumIteration();
//		demoRepeatWithCondition();
//		demoRepeatWithSupplier();
//		demoRepeatWhen();
		demoRepeatWhenWithEnd();
	}
	
	private static void demoRepeatNumIteration() {
		//Il Mono originale diventa un Flux con l'applicazione dell'operatore repeat
		getCountryName().repeat(3) //Vengono generati 4 elementi perché l'argomento di repeat indica il numero di ripetizioni successive alla prima emissione di default
						.subscribe(Util.subscriber());
	}
	
	private static void demoRepeatWithCondition() {
		getCountryName().repeat()
						.takeUntil("Italy"::equalsIgnoreCase)
						.subscribe(Util.subscriber());
	}
	
	private static void demoRepeatWithSupplier() {
		AtomicInteger count = new AtomicInteger(0);
		getCountryName().repeat(() -> count.incrementAndGet() < 5)
						.subscribe(Util.subscriber());
	}
	
	private static void demoRepeatWhen() {
		getCountryName().repeatWhen(flux -> flux.delayElements(Duration.ofMillis(500)))
						.subscribe(Util.subscriber());
		Util.sleepSeconds(10);
	}
	
	private static void demoRepeatWhenWithEnd() {
		getCountryName().repeatWhen(flux -> flux.delayElements(Duration.ofMillis(500))
																				  .take(5))
			.subscribe(Util.subscriber());
		Util.sleepSeconds(10);
	}
	
	private static Mono<String> getCountryName() {
		return Mono.fromSupplier(() -> Util.faker().country().name());
	}
}
