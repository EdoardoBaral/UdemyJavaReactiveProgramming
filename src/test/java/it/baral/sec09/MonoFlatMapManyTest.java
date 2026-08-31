package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("MonoFlatMapMany Tests")
class MonoFlatMapManyTest {

	@Test
	@DisplayName("flatMapMany() trasforma il singolo valore emesso in un Flux di piu' elementi")
	void testFlatMapManyTransformsMonoValueIntoMultiElementFlux() {
		StepVerifier.create(Mono.just(3).flatMapMany(n -> Flux.range(1, n)))
					.expectNext(1, 2, 3)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("flatMapMany() su un Mono vuoto risulta in un Flux vuoto")
	void testFlatMapManyOnEmptyMonoResultsInEmptyFlux() {
		StepVerifier.create(Mono.<Integer>empty().flatMapMany(n -> Flux.range(1, n)))
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("flatMapMany() propaga un errore emesso dal Flux restituito dal mapper")
	void testFlatMapManyPropagatesErrorFromMapper() {
		StepVerifier.create(Mono.just(1).flatMapMany(n -> Flux.error(new RuntimeException("boom"))))
					.expectError(RuntimeException.class)
					.verify();
	}
}
