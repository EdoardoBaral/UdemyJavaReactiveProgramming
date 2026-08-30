package it.baral.sec05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DisplayName("SwitchIfEmpty Tests")
class SwitchIfEmptyTest {

	@Test
	@DisplayName("switchIfEmpty() sostituisce l'intero Flux con quello di fallback quando il Flux a monte e' vuoto")
	void testSwitchIfEmptyReplacesFluxWithFallbackWhenUpstreamIsEmpty() {
		Flux<Integer> flux = Flux.range(1, 10)
								 .filter(i -> i > 10)
								 .switchIfEmpty(Flux.just(-1));

		StepVerifier.create(flux)
					.expectNext(-1)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("switchIfEmpty() viene ignorato quando il Flux a monte emette gia' degli elementi")
	void testSwitchIfEmptyIsIgnoredWhenUpstreamAlreadyEmitsElements() {
		Flux<Integer> flux = Flux.range(1, 10)
								 .filter(i -> i < 6)
								 .switchIfEmpty(Flux.just(-1));

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("switchIfEmpty() con un Flux di fallback che emette piu' elementi li emette tutti")
	void testSwitchIfEmptyWithMultiElementFallbackEmitsAllOfThem() {
		Flux<Integer> flux = Flux.<Integer>empty()
								 .switchIfEmpty(Flux.just(10, 20, 30));

		StepVerifier.create(flux)
					.expectNext(10, 20, 30)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("switchIfEmpty() propaga un errore emesso dal Flux di fallback")
	void testSwitchIfEmptyPropagatesErrorFromFallbackFlux() {
		Flux<Integer> flux = Flux.<Integer>empty()
								 .switchIfEmpty(Flux.error(new RuntimeException("fallback error")));

		StepVerifier.create(flux)
					.expectError(RuntimeException.class)
					.verify();
	}
}
