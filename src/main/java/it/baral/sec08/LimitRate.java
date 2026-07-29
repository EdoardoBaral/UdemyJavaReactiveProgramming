package it.baral.sec08;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class LimitRate {
	
	private static final Logger log = LoggerFactory.getLogger(LimitRate.class);
	
	public static void main(String[] args) {
		Flux<Integer> producer = Flux.generate(() -> 1,
											   (state, sink) -> {
													log.info("generating: {}", state);
													sink.next(state);
													return ++state;
											   })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.limitRate(5)
				.publishOn(Schedulers.boundedElastic())
				.map(LimitRate::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	private static int timeConsumingTask(int i) {
		log.info("{}", i);
		Util.sleepSeconds(1);
		return i;
	}
}
