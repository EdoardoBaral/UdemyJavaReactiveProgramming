package it.baral.sec09;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

public class Then {
	
	private static final Logger log = LoggerFactory.getLogger(Then.class);
	
	public static void main(String[] args) {
		List<String> records = List.of("a", "b", "c");
		
		saveRecords(records).then(sendNotification(records))
							.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
	
	private static Flux<String> saveRecords(List<String> records) {
		return Flux.fromIterable(records)
				   .map(r -> "saved "+ r)
				   .delayElements(Duration.ofMillis(500));
	}
	
	private static Mono<Void> sendNotification(List<String> records) {
		return Mono.fromRunnable(() -> log.info("All these records saved successfully: {}", records));
	}
}
