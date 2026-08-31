package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ConcatError Tests")
class ConcatErrorTest {

	@Test
	@DisplayName("concatWith() interrompe subito la sequenza al primo errore, senza sottoscrivere le sorgenti successive")
	void testConcatWithStopsImmediatelyOnErrorAndSkipsSubsequentSource() {
		AtomicBoolean secondSubscribed = new AtomicBoolean();
		Flux<Integer> flux = Flux.just(1, 2, 3)
								  .concatWith(Flux.error(new RuntimeException("boom")))
								  .concatWith(Flux.just(4, 5, 6).doOnSubscribe(s -> secondSubscribed.set(true)));

		StepVerifier.create(flux)
					.expectNext(1, 2, 3)
					.expectError(RuntimeException.class)
					.verify();

		assertFalse(secondSubscribed.get());
	}

	@Test
	@DisplayName("Flux.concatDelayError() sottoscrive comunque tutte le sorgenti, propagando l'errore solo al termine")
	void testConcatDelayErrorSubscribesAllSourcesAndDelaysErrorToTheEnd() {
		AtomicBoolean thirdSubscribed = new AtomicBoolean();
		Flux<Integer> flux = Flux.concatDelayError(Flux.just(1, 2, 3),
													Flux.error(new RuntimeException("boom")),
													Flux.just(4, 5, 6).doOnSubscribe(s -> thirdSubscribed.set(true)));

		StepVerifier.create(flux)
					.expectNext(1)
					.expectNextCount(5)
					.expectError(RuntimeException.class)
					.verify();

		assertTrue(thirdSubscribed.get());
	}
}
