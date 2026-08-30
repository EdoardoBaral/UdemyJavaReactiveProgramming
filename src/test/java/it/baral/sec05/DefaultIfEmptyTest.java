package it.baral.sec05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DisplayName("DefaultIfEmpty Tests")
class DefaultIfEmptyTest {

	@Test
	@DisplayName("defaultIfEmpty() emette il valore di default quando il Flux a monte e' vuoto")
	void testDefaultIfEmptyEmitsDefaultValueWhenUpstreamIsEmpty() {
		Flux<Integer> flux = Flux.range(1, 10)
								 .filter(i -> i > 10)
								 .defaultIfEmpty(-1);

		StepVerifier.create(flux)
					.expectNext(-1)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("defaultIfEmpty() viene ignorato quando il Flux a monte emette gia' degli elementi")
	void testDefaultIfEmptyIsIgnoredWhenUpstreamAlreadyEmitsElements() {
		Flux<Integer> flux = Flux.range(1, 10)
								 .defaultIfEmpty(-1);

		StepVerifier.create(flux)
					.expectNext(1)
					.expectNextCount(9)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("defaultIfEmpty() propaga un errore invece del valore di default")
	void testDefaultIfEmptyPropagatesErrorInsteadOfDefaultValue() {
		Flux<Integer> flux = Flux.<Integer>error(new RuntimeException("boom"))
								 .defaultIfEmpty(-1);

		StepVerifier.create(flux)
					.expectError(RuntimeException.class)
					.verify();
	}
}
