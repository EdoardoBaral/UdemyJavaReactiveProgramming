package it.baral.sec02;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class MonoFromFuture {
	
	private static final Logger log = LoggerFactory.getLogger(MonoFromFuture.class);
	
	public static void main(String[] args) throws InterruptedException{
		Mono.fromFuture(getName())
			.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
	
	private static CompletableFuture<String> getName() {
		return CompletableFuture.supplyAsync(() -> {
			log.info("Generating name");
			return Util.faker().name().fullName();
		});
	}
}
