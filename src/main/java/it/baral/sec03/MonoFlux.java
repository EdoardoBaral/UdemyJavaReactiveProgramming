package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MonoFlux {
	
	public static void main(String[] args) {
		save(Flux.from(getUsername(1)));
		save(Flux.from(getUsername(2)));
		save(Flux.from(getUsername(3)));
		
		Flux.range(1, 10)
			.next()
			.subscribe(Util.subscriber());
	}
	
	private static Mono<String> getUsername(int userId) {
		return switch(userId) {
			case 1 -> Mono.just("Edoardo");
			case 2 -> Mono.empty();
			default -> Mono.error(new IllegalArgumentException("Invalid input"));
		};
	}
	
	private static void save(Flux<String> flux) {
		flux.subscribe(Util.subscriber());
	}
}
