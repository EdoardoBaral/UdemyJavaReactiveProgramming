package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DisplayName("MonoFlatMap Tests")
class MonoFlatMapTest {

	@Test
	@DisplayName("flatMap() trasforma il valore emesso in un nuovo Mono")
	void testFlatMapTransformsEmittedValueIntoNewMono() {
		StepVerifier.create(Mono.just(1).flatMap(v -> Mono.just("value-" +v)))
					.expectNext("value-1")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("flatMap() su un Mono vuoto risulta in un Mono vuoto, senza invocare il mapper")
	void testFlatMapOnEmptyMonoResultsInEmptyMono() {
		StepVerifier.create(Mono.<Integer>empty().flatMap(v -> Mono.just("value-" +v)))
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("flatMap() propaga un errore emesso dal Mono restituito dal mapper")
	void testFlatMapPropagatesErrorFromMapper() {
		StepVerifier.create(Mono.just(1).flatMap(v -> Mono.error(new RuntimeException("boom"))))
					.expectError(RuntimeException.class)
					.verify();
	}

	@Test
	@DisplayName("flatMap() propaga un errore della sorgente senza invocare il mapper")
	void testFlatMapPropagatesUpstreamErrorWithoutInvokingMapper() {
		AtomicBoolean mapperInvoked = new AtomicBoolean();

		StepVerifier.create(Mono.<Integer>error(new RuntimeException("boom")).flatMap(v -> {
						 mapperInvoked.set(true);
						 return Mono.just("x");
					 }))
					.expectError(RuntimeException.class)
					.verify();

		assertFalse(mapperInvoked.get());
	}
}
