package it.baral.sec13;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class Propagation {
	
	private static final Logger log = LoggerFactory.getLogger(Propagation.class);
	
	public static void main(String[] args) {
		contextSuccess();
		
		Util.sleepSeconds(5);
	}
	
	private static void contextSuccess() {
		getWelcomeMessage().concatWith(Flux.merge(producer1(), producer2()))
						   .contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
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
	
	private static Mono<String> producer1() {
		return Mono.<String>deferContextual(ctx -> {
					    log.info("producer1 context: {}", ctx);
					    return Mono.empty();
				    })
				   .subscribeOn(Schedulers.boundedElastic());
	}
	
	private static Mono<String> producer2() {
		return Mono.<String>deferContextual(ctx -> {
						log.info("producer2 context: {}", ctx);
						return Mono.empty();
					})
				   .subscribeOn(Schedulers.parallel());
	}
}
