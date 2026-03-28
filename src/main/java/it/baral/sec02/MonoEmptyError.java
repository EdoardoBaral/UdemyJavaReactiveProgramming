package it.baral.sec02;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class MonoEmptyError {
	
	private static final Logger log = LoggerFactory.getLogger(MonoEmptyError.class);
	
	public static void main(String[] args) {
		getUsername(1).subscribe(Util.subscriber());
		System.out.println();
		
		getUsername(2).subscribe(Util.subscriber("Subscriber1"));
		System.out.println();
		
		getUsername(3).subscribe(Util.subscriber("Subscriber2"));
		System.out.println();
		
		getUsername(3).subscribe(System.out::println);
		System.out.println();
	}
	
	private static Mono<String> getUsername(int userId) {
		return switch(userId) {
			case 1 -> Mono.just("Edoardo");
			case 2 -> Mono.empty();
			default -> Mono.error(new IllegalArgumentException("Invalid input"));
		};
	}
}
