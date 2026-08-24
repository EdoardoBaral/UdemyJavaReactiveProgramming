package it.baral.sec13;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class Context {
	
	private static final Logger log = LoggerFactory.getLogger(Context.class);
	
	public static void main(String[] args) {
//		contextFailure();
		contextSuccess();
	}
	
	private static void contextFailure() {
		getWelcomeMessage().contextWrite(reactor.util.context.Context.of("a", "b"))
						   .subscribe(Util.subscriber());
	}
	
	private static void contextSuccess() {
		getWelcomeMessage().contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
			.subscribe(Util.subscriber());
	}
	
	private static Mono<String> getWelcomeMessage() {
		return Mono.deferContextual(ctx -> {
			if(ctx.hasKey("user")) {
				return Mono.just("welcome %s".formatted(ctx.get("user").toString()));
			} else {
				return Mono.error(new RuntimeException("unauthorized"));
			}
		});
	}
}
