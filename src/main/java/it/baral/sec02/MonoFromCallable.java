package it.baral.sec02;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

public class MonoFromCallable {
	
	private static final Logger logger = LoggerFactory.getLogger(MonoFromCallable.class);
	
	public static void main(String[] args) {
		List<Integer> list = List.of(1, 2, 3, 4, 5);
		Mono.fromCallable(() -> sum(list))
			.subscribe(Util.subscriber());
	}
	
	private static int sum(List<Integer> list) {
		logger.info("Calculating sum of {}", list);
		return list.stream()
				   .mapToInt(Integer::intValue)
				   .sum();
	}
}
