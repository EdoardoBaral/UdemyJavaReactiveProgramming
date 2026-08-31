package it.baral.sec07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Parallel Tests")
class ParallelTest {

	@Test
	@DisplayName("parallel().runOn().sequential() elabora tutti gli elementi, indipendentemente dall'ordine di completamento")
	void testParallelProcessingReturnsAllProcessedElementsRegardlessOfOrder() {
		List<Integer> processed = Flux.range(1, 10)
									   .parallel(4)
									   .runOn(Schedulers.parallel())
									   .map(i -> i *2)
									   .sequential()
									   .collectSortedList()
									   .block();

		assertEquals(List.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20), processed);
	}

	@Test
	@DisplayName("parallel().runOn(Schedulers.parallel()) distribuisce l'elaborazione su piu' thread")
	void testParallelProcessingUsesMultipleThreads() {
		Set<String> threadNames = ConcurrentHashMap.newKeySet();

		Flux.range(1, 8)
			.parallel(4)
			.runOn(Schedulers.parallel())
			.doOnNext(i -> threadNames.add(Thread.currentThread().getName()))
			.sequential()
			.blockLast();

		assertTrue(threadNames.size() > 1, "L'elaborazione deve avvenire su piu' thread paralleli");
		assertTrue(threadNames.stream().allMatch(name -> name.startsWith("parallel-")));
	}
}
