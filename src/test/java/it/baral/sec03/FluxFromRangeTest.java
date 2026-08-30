package it.baral.sec03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DisplayName("FluxFromRange Tests")
class FluxFromRangeTest {

	@Test
	@DisplayName("range() emette gli interi consecutivi a partire dal valore iniziale")
	void testRangeEmitsConsecutiveIntegers() {
		Flux<Integer> flux = Flux.range(1, 10);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("range() con count zero non emette alcun elemento")
	void testRangeWithZeroCountEmitsNothing() {
		Flux<Integer> flux = Flux.range(1, 0);

		StepVerifier.create(flux)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("range() con count uno emette un solo elemento")
	void testRangeWithSingleCount() {
		Flux<Integer> flux = Flux.range(5, 1);

		StepVerifier.create(flux)
					.expectNext(5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("range() mappato conserva il numero di elementi emessi")
	void testRangeMappedPreservesElementCount() {
		Flux<String> flux = Flux.range(1, 10)
								 .map(x -> "value-" + x);

		StepVerifier.create(flux)
					.expectNextCount(10)
					.expectComplete()
					.verify();
	}
}
