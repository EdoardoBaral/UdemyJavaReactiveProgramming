package it.baral.sec08;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class FluxCreate {
	
	private static final Logger log = LoggerFactory.getLogger(FluxCreate.class);
	
	public static void main(String[] args) {
		System.setProperty("reactor.bufferSize.small", "16");
		
		Flux<Integer> producer = Flux.create((sink) -> {
									     for(int i=1; i<=500 && !sink.isCancelled(); i++) {
										   log.info("generating: {}", i);
										   Util.sleep(Duration.ofMillis(50));
										   sink.next(i);
									     }
									     sink.complete();
								     })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.publishOn(Schedulers.boundedElastic())
				.map(FluxCreate::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	private static int timeConsumingTask(int i) {
		log.info("received: {}", i);
		Util.sleepSeconds(1);
		return i;
	}
}
