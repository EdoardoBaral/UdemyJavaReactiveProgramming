package it.baral.sec03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("MonoFlux Tests")
class MonoFluxTest {

	@Test
	@DisplayName("Flux.from() su un Mono con valore emette quel valore e completa")
	void testFluxFromMonoWithValueEmitsValue() {
		Mono<String> mono = Mono.just("Edoardo");
		Flux<String> flux = Flux.from(mono);

		StepVerifier.create(flux)
					.expectNext("Edoardo")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Flux.from() su un Mono vuoto completa senza emettere elementi")
	void testFluxFromEmptyMonoCompletesWithoutElements() {
		Mono<String> mono = Mono.empty();
		Flux<String> flux = Flux.from(mono);

		StepVerifier.create(flux)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Flux.from() su un Mono in errore termina con lo stesso errore")
	void testFluxFromErrorMonoTerminatesWithSameError() {
		Mono<String> mono = Mono.error(new IllegalArgumentException("Invalid input"));
		Flux<String> flux = Flux.from(mono);

		StepVerifier.create(flux)
					.expectError(IllegalArgumentException.class)
					.verify();
	}

	@Test
	@DisplayName("next() su un Flux restituisce un Mono con il primo elemento")
	void testNextReturnsMonoWithFirstElement() {
		Mono<Integer> mono = Flux.range(1, 10)
								  .next();

		StepVerifier.create(mono)
					.expectNext(1)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("next() su un Flux vuoto restituisce un Mono vuoto")
	void testNextOnEmptyFluxReturnsEmptyMono() {
		Mono<Integer> mono = Flux.<Integer>empty().next();

		StepVerifier.create(mono)
					.expectComplete()
					.verify();
	}
}
