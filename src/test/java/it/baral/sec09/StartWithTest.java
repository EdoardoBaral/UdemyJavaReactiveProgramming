package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("StartWith Tests")
class StartWithTest {

	@Test
	@DisplayName("startWith(valori) antepone i valori indicati alla sequenza, comparendo prima degli elementi originali")
	void testStartWithValuesFollowedByTakeIncludesPrependedValuesFirst() {
		StepVerifier.create(Flux.just(1, 2, 3).startWith(-1, 0).take(3))
					.expectNext(-1, 0, 1)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("startWith(Iterable) antepone tutti gli elementi dell'Iterable alla sequenza originale")
	void testStartWithIterablePrependsAllIterableElements() {
		StepVerifier.create(Flux.just(1, 2, 3).startWith(List.of(-1, 0)))
					.expectNext(-1)
					.expectNextCount(4)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("startWith(Publisher) antepone l'intera sequenza del Flux indicato alla sorgente originale")
	void testStartWithPublisherPrependsEntireSequenceOfGivenFlux() {
		StepVerifier.create(Flux.just(1, 2, 3).startWith(Flux.just(4, 5, 6)))
					.expectNext(4)
					.expectNextCount(5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Piu' startWith() concatenati anticipano il risultato: l'ultimo applicato compare per primo")
	void testMultipleStartWithAppliesLastAppliedFirst() {
		List<Integer> result = Flux.just(1, 2, 3)
									.startWith(Flux.just(4, 5, 6))
									.startWith(1000)
									.collectList()
									.block();

		assertEquals(List.of(1000, 4, 5, 6, 1, 2, 3), result);
	}
}
