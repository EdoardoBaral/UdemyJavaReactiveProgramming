package it.baral.sec06;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class HotPublisherCache {
	
	private static final Logger log = LoggerFactory.getLogger(HotPublisherCache.class);
	
	public static void main(String[] args) {
		Flux<Integer> stockFlux = stockStream().replay(10).autoConnect(0);
		Util.sleepSeconds(4);
		log.info("sub1 is joining");
		stockFlux.subscribe(Util.subscriber("sub1"));
		
		Util.sleepSeconds(4);
		log.info("sub2 is joining");
		stockFlux.subscribe(Util.subscriber("sub2"));
		
		Util.sleepSeconds(15);
	}
	
	private static Flux<Integer> stockStream() {
		return Flux.generate(sink -> sink.next(Util.faker().random().nextInt(1, 100)))
				   .delayElements(Duration.ofSeconds(3))
				   .doOnNext(price -> log.info("emitting price: {}", price))
				   .cast(Integer.class);
	}
}
