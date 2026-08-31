package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("FluxFlatMapAssignment Tests")
class FluxFlatMapAssignmentTest {

	@Test
	@DisplayName("flatMap(mapper, concurrency) limita il numero massimo di sorgenti interne sottoscritte contemporaneamente")
	void testFlatMapWithConcurrencyLimitsMaxConcurrentInnerSubscriptions() {
		int concurrency = 2;
		List<String> events = Collections.synchronizedList(new ArrayList<>());
		Flux<Integer> flux = Flux.range(1, 10)
								  .flatMap(i -> Flux.just(i)
													 .delayElements(Duration.ofMillis(15))
													 .doOnSubscribe(s -> events.add("subscribe-" +i))
													 .doOnComplete(() -> events.add("complete-" +i)), concurrency);

		StepVerifier.create(flux)
					.expectNextCount(10)
					.expectComplete()
					.verify();

		int maxConcurrentSources = maxConcurrentSubscriptions(events);
		assertTrue(maxConcurrentSources <= concurrency, "Non devono mai essere attive piu' di " +concurrency+ " sorgenti contemporaneamente");
		assertTrue(maxConcurrentSources > 1, "Devono comunque risultare attive piu' sorgenti in contemporanea");
	}

	@Test
	@DisplayName("flatMap(mapper, 1) elabora le sorgenti interne una alla volta, senza mai sovrapporle")
	void testFlatMapWithConcurrencyOfOneProcessesSourcesSequentially() {
		List<String> events = Collections.synchronizedList(new ArrayList<>());
		Flux<Integer> flux = Flux.range(1, 5)
								  .flatMap(i -> Flux.just(i)
													 .delayElements(Duration.ofMillis(10))
													 .doOnSubscribe(s -> events.add("subscribe-" +i))
													 .doOnComplete(() -> events.add("complete-" +i)), 1);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();

		assertTrue(maxConcurrentSubscriptions(events) <= 1, "Con concorrenza 1 non devono mai risultare piu' sorgenti attive contemporaneamente");
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
