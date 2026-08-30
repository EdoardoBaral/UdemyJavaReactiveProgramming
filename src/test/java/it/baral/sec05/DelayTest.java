package it.baral.sec05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@DisplayName("Delay Tests")
class DelayTest {

	@Test
	@DisplayName("delayElements() ritarda l'emissione di ciascun elemento della durata indicata")
	void testDelayElementsDelaysEachElementByGivenDuration() {
		StepVerifier.withVirtualTime(() -> Flux.range(1, 3)
											   .delayElements(Duration.ofSeconds(1)))
					.expectSubscription()
					.expectNoEvent(Duration.ofSeconds(1))
					.expectNext(1)
					.expectNoEvent(Duration.ofSeconds(1))
					.expectNext(2)
					.expectNoEvent(Duration.ofSeconds(1))
					.expectNext(3)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("delayElements() non altera il numero di elementi emessi")
	void testDelayElementsDoesNotAlterElementCount() {
		StepVerifier.withVirtualTime(() -> Flux.range(1, 10)
											   .delayElements(Duration.ofSeconds(1)))
					.expectSubscription()
					.thenAwait(Duration.ofSeconds(10))
					.expectNextCount(10)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("delayElements() non emette nulla prima che trascorra il ritardo")
	void testDelayElementsEmitsNothingBeforeDelayElapses() {
		StepVerifier.withVirtualTime(() -> Flux.just(1)
											   .delayElements(Duration.ofSeconds(1)))
					.expectSubscription()
					.expectNoEvent(Duration.ofMillis(999))
					.thenAwait(Duration.ofMillis(1))
					.expectNext(1)
					.expectComplete()
					.verify();
	}
}
