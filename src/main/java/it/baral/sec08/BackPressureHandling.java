package it.baral.sec08;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class BackPressureHandling {

	private static final Logger log = LoggerFactory.getLogger(BackPressureHandling.class);
	
	public static void main(String[] args) {
		System.setProperty("reactor.bufferSize.small", "16");
		
		Flux<Integer> producer = Flux.generate(() -> 1,
											   (state, sink) -> {
												   log.info("generating: {}", state);
												   sink.next(state);
												   return ++state;
											   })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.publishOn(Schedulers.boundedElastic())
				.map(BackPressureHandling::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	private static int timeConsumingTask(int i) {
		Util.sleepSeconds(1);
		return i;
	}
}
