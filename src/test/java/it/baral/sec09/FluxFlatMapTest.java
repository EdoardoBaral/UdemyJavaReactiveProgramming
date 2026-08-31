package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("FluxFlatMap Tests")
class FluxFlatMapTest {

	@Test
	@DisplayName("flatMap() appiattisce in un unico Flux i risultati di tutte le sorgenti interne")
	void testFlatMapFlattensAllInnerSourcesResults() {
		List<Integer> result = Flux.range(1, 3)
									.flatMap(i -> Flux.just(i *10, i *10 +1))
									.collectList()
									.block();

		assertEquals(6, result.size());
		assertTrue(result.containsAll(List.of(10, 11, 20, 21, 30, 31)));
	}

	@Test
	@DisplayName("flatMap() sottoscrive piu' sorgenti interne contemporaneamente, a differenza di concatMap")
	void testFlatMapSubscribesToInnerSourcesConcurrently() {
		List<String> events = Collections.synchronizedList(new ArrayList<>());
		Flux<Integer> flux = Flux.range(1, 5)
								  .flatMap(i -> Flux.just(i)
													 .delayElements(Duration.ofMillis(20))
													 .doOnSubscribe(s -> events.add("subscribe-" +i))
													 .doOnComplete(() -> events.add("complete-" +i)));

		StepVerifier.create(flux)
					.expectNextCount(5)
					.expectComplete()
					.verify();

		assertTrue(maxConcurrentSubscriptions(events) > 1, "flatMap deve sottoscrivere piu' sorgenti interne contemporaneamente");
	}

	private int maxConcurrentSubscriptions(List<String> events) {
		int active = 0;
		int max = 0;
		for(String event : events) {
			active += event.startsWith("subscribe-") ? 1 : -1;
			max = Math.max(max, active);
		}
		return max;
	}
}
