package it.baral.sec03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DisplayName("FluxEmptyError Tests")
class FluxEmptyErrorTest {

	@Test
	@DisplayName("empty() completa immediatamente senza emettere elementi")
	void testEmptyCompletesWithoutElements() {
		Flux<Object> flux = Flux.empty();

		StepVerifier.create(flux)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("error() termina immediatamente con l'eccezione fornita")
	void testErrorTerminatesWithGivenException() {
		Exception exception = new Exception("Oops");
		Flux<Object> flux = Flux.error(exception);

		StepVerifier.create(flux)
					.expectErrorMatches(err -> err == exception)
					.verify();
	}

	@Test
	@DisplayName("error() non emette alcun elemento prima dell'errore")
	void testErrorEmitsNoElementsBeforeFailing() {
		Flux<Integer> flux = Flux.error(new RuntimeException("failure"));

		StepVerifier.create(flux)
					.expectError(RuntimeException.class)
					.verify();
	}
}
