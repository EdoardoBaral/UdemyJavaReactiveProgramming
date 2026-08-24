package it.baral.sec13;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class ContextAppendUpdate {
	
	private static final Logger log = LoggerFactory.getLogger(ContextAppendUpdate.class);
	
	public static void main(String[] args) {
//		contextAppend();
//		contextAppendEmpty();
		contextUpdate();
	}
	
	private static void contextAppend() {
		getWelcomeMessage().contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
						   .contextWrite(reactor.util.context.Context.of("a", "b").put("c", "d").put("e", "f"))
						   .subscribe(Util.subscriber());
	}
	
	private static void contextAppendEmpty() {
		getWelcomeMessage().contextWrite(ctx -> reactor.util.context.Context.empty())
						   .contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
						   .contextWrite(reactor.util.context.Context.of("a", "b").put("c", "d").put("e", "f"))
						   .subscribe(Util.subscriber());
	}
	
	private static void contextUpdate() {
		getWelcomeMessage().contextWrite(reactor.util.context.Context.of("user", "Paolo"))
						   .contextWrite(ctx -> ctx.delete("c"))
						   .contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
						   .contextWrite(reactor.util.context.Context.of("a", "b").put("c", "d").put("e", "f"))
						   .subscribe(Util.subscriber());
	}
	
	private static Mono<String> getWelcomeMessage() {
		return Mono.deferContextual(ctx -> {
			log.info("{}", ctx);
			if(ctx.hasKey("user")) {
				return Mono.just("welcome %s".formatted(ctx.get("user").toString()));
			} else {
				return Mono.error(new RuntimeException("unauthorized"));
			}
		});
	}
}
