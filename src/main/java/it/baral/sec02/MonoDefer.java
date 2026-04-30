package it.baral.sec02;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

public class MonoDefer {
	
	private static final Logger log = LoggerFactory.getLogger(MonoDefer.class);
	
	public static void main(String[] args) {
		Mono.defer(MonoDefer::createPublisher)
			.subscribe(Util.subscriber());
	}
	
	private static Mono<Integer> createPublisher() {
		log.info("Creating publisher");
		List<Integer> list = List.of(1, 2, 3, 4, 5);
		Util.sleepSeconds(3);
		
		return Mono.fromSupplier(() -> sum(list));
	}
	
	//Time consuming business logic
	private static int sum(List<Integer> list) {
		log.info("Calculating sum of {}", list);
		Util.sleepSeconds(3);
		return list.stream()
				   .mapToInt(Integer::intValue)
				   .sum();
	}
}
