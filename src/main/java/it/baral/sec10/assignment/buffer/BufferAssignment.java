package it.baral.sec10.assignment.buffer;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BufferAssignment {
	
	public static void main(String[] args) {
		Set<String> allowedCategories = Set.of("Science fiction", "Fantasy", "Suspense/Thriller");
		
		orderStream().filter(o -> allowedCategories.contains(o.genre()))
			.buffer(Duration.ofSeconds(5))
			.map(BufferAssignment::generateReport)
			.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	private static Flux<BookOrder> orderStream() {
		return Flux.interval(Duration.ofMillis(200))
				   .map(i -> BookOrder.create());
	}
	
	private static RevenueReport generateReport(List<BookOrder> orders) {
		Map<String, Integer> revenue = orders.stream()
									   		 .collect(Collectors.groupingBy(BookOrder::genre, Collectors.summingInt(BookOrder::price)));
		return new RevenueReport(LocalDateTime.now(), revenue);
	}
}
