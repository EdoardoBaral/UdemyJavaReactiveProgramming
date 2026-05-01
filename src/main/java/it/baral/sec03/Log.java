package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

public class Log {
	
	public static void main(String[] args) {
		Flux.range(1, 10)
			.log()
			.map(x -> Util.faker().name().name())
			.log("map-log")
			.subscribe(Util.subscriber("Sub1"));
	}
}
