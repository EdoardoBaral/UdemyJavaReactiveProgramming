package it.baral.sec04;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

public class TakeOperator {
	
	public static void main(String[] args) {
		//take();
		//takeWhile();
		takeUntil();
	}
	
	private static void take() {
		Flux.range(1, 10)
			.log("take")
			.take(3)
			.log("sub")
			.subscribe(Util.subscriber());
	}
	
	private static void takeWhile() {
		Flux.range(1, 10)
			.log("take")
			.takeWhile(i -> i < 6)
			.log("sub")
			.subscribe(Util.subscriber());
	}
	
	private static void takeUntil() {
		Flux.range(1, 10)
			.log("take")
			.takeUntil(i -> i == 6)
			.log("sub")
			.subscribe(Util.subscriber());
	}
}
