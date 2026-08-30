package it.baral.sec03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@DisplayName("FluxInterval Tests")
class FluxIntervalTest {

	@Test
	@DisplayName("interval() emette valori incrementali a partire da zero")
	void testIntervalEmitsIncrementalValuesStartingFromZero() {
		StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofMillis(500)).take(3))
					.expectSubscription()
					.expectNoEvent(Duration.ofMillis(500))
					.expectNext(0L)
					.expectNoEvent(Duration.ofMillis(500))
					.expectNext(1L)
					.expectNoEvent(Duration.ofMillis(500))
					.expectNext(2L)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("interval() mappato conserva il numero di tick emessi")
	void testIntervalMappedPreservesTickCount() {
		StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofMillis(500))
												.map(x -> "tick-" + x)
												.take(5))
					.expectSubscription()
					.thenAwait(Duration.ofSeconds(3))
					.expectNextCount(5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("interval() non emette nulla prima del primo intervallo")
	void testIntervalEmitsNothingBeforeFirstTick() {
		StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofMillis(500)).take(1))
					.expectSubscription()
					.expectNoEvent(Duration.ofMillis(499))
					.thenAwait(Duration.ofMillis(1))
					.expectNext(0L)
					.expectComplete()
					.verify();
	}
}
