package it.baral.sec04;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class FluxGenerate {
	
	private static final Logger log = LoggerFactory.getLogger(FluxGenerate.class);
	
	public static void main(String[] args) {
		//generateEndlessLoop();
		//generateLimitedItems();
		generateSingleItem();
	}
	
	private static void generateEndlessLoop() {
		Flux.generate(synchronousSink -> {
				log.info("invoked");
				synchronousSink.next(1);
			})
			.subscribe(Util.subscriber());
	}
	
	private static void generateLimitedItems() {
		Flux.generate(synchronousSink -> {
				log.info("invoked");
				synchronousSink.next(1);
			})
			.take(4)
			.subscribe(Util.subscriber());
	}
	
	private static void generateSingleItem() {
		Flux.generate(synchronousSink -> {
				log.info("invoked");
				synchronousSink.next(1);
				synchronousSink.complete();
			})
			.subscribe(Util.subscriber());
	}
}
