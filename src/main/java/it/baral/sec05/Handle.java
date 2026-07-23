package it.baral.sec05;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

public class Handle {
	
	public static void main(String[] args) {
//		handle();
		assignment();
	}
	
	private static void handle() {
		Flux.range(1, 10)
//			.filter(i -> i != 7)
			.handle((item, sink) -> {
				switch(item) {
					case 1 -> sink.next(-2);
					case 4 -> {}
					case 7 -> sink.error(new Exception("ooops"));
					default -> sink.next(item);
				}
			})
			.cast(Integer.class)
			.subscribe(Util.subscriber());
	}
	
	private static void assignment() {
		Flux.generate(sink -> {
				String country = Util.faker().country().name();
				sink.next(country);
			})
			.cast(String.class)
			.handle((item, sink) -> {
				sink.next(item);
				if(item.equalsIgnoreCase("Canada")) {
					sink.complete();
				}
			})
			.subscribe(Util.subscriber());
	}
}
