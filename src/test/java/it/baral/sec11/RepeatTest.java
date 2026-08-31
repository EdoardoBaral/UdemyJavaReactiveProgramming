package it.baral.sec11;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@DisplayName("Repeat Tests")
class RepeatTest {

	@Test
	@DisplayName("repeat(n) ripete la sottoscrizione n volte oltre alla prima emissione")
	void testRepeatWithCountRepeatsGivenNumberOfTimesBeyondFirstEmission() {
		AtomicInteger counter = new AtomicInteger();
		Mono<Integer> mono = Mono.fromSupplier(counter::incrementAndGet);

		StepVerifier.create(mono.repeat(3))
					.expectNext(1, 2, 3, 4)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("repeat(BooleanSupplier) interrompe la ripetizione non appena il supplier restituisce false")
	void testRepeatWithConditionStopsWhenSupplierReturnsFalse() {
		AtomicInteger counter = new AtomicInteger();
		AtomicInteger repeatCheckCount = new AtomicInteger();
		Mono<Integer> mono = Mono.fromSupplier(counter::incrementAndGet);

		StepVerifier.create(mono.repeat(() -> repeatCheckCount.incrementAndGet() < 3))
					.expectNext(1, 2, 3)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("repeat() illimitato combinato con takeUntil() si interrompe al verificarsi della condizione")
	void testRepeatWithUnboundedRepetitionStopsWhenTakeUntilConditionMet() {
		AtomicInteger counter = new AtomicInteger();
		Mono<Integer> mono = Mono.fromSupplier(counter::incrementAndGet);

		StepVerifier.create(mono.repeat().takeUntil(v -> v == 4))
					.expectNext(1, 2, 3, 4)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("repeatWhen() e' guidato dal Flux di trigger indicato e puo' essere limitato applicando take() al trigger")
	void testRepeatWhenIsDrivenByTriggerFluxAndCanBeLimitedWithTake() {
		Mono<Integer> mono = Mono.fromSupplier(new AtomicInteger()::incrementAndGet);

		StepVerifier.withVirtualTime(() -> mono.repeatWhen(flux -> flux.delayElements(Duration.ofMillis(500)).take(2)))
					.thenAwait(Duration.ofSeconds(2))
					.expectNext(1, 2, 3)
					.expectComplete()
					.verify();
	}
}
