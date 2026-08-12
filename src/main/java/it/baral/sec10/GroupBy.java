package it.baral.sec10;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.time.Duration;

public class GroupBy {
	
	private static final Logger log = LoggerFactory.getLogger(GroupBy.class);
	
	public static void main(String[] args) {
//		groupByWithAllNumbers();
		groupByWithOnlyEvenNumbers();
		
		Util.sleepSeconds(60);
	}
	
	private static void groupByWithAllNumbers() {
		Flux.range(1, 30)
			.delayElements(Duration.ofSeconds(1))
			.groupBy(i -> i % 2 == 0) //In questo modo la chiave di raggruppamento sarà Boolean. Potrei anche specificare i%2 e farla diventare Integer, è indifferente
			.flatMap(GroupBy::processEvents)
			.subscribe();
	}
	
	private static void groupByWithOnlyEvenNumbers() {
		Flux.range(1, 30)
			.delayElements(Duration.ofSeconds(1))
			.map(i -> i * 2)
			.groupBy(i -> i % 2 == 0)
			.flatMap(GroupBy::processEvents)
			.subscribe();
	}
	
	private static Mono<Void> processEvents(GroupedFlux<Boolean, Integer> groupedFlux) {
		log.info("recieived flux for {}", groupedFlux.key());
		return groupedFlux.doOnNext(i -> log.info("key: {}, value: {}", groupedFlux.key(), i))
						  .doOnComplete(() -> log.info("{} completed", groupedFlux.key()))
						  .then();
	}
}
