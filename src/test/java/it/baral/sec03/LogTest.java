package it.baral.sec03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DisplayName("Log Tests")
class LogTest {

	@Test
	@DisplayName("log() non altera gli elementi emessi dal Flux")
	void testLogDoesNotAlterEmittedElements() {
		Flux<Integer> flux = Flux.range(1, 10)
								  .log();

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("log() con categoria non altera gli elementi emessi dal Flux")
	void testLogWithCategoryDoesNotAlterEmittedElements() {
		Flux<Integer> flux = Flux.range(1, 5)
								  .log("map-log");

		StepVerifier.create(flux)
					.expectNextCount(5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("log() posizionato dopo map() non altera la trasformazione applicata")
	void testLogAfterMapDoesNotAlterTransformation() {
		Flux<String> flux = Flux.range(1, 3)
								 .log()
								 .map(x -> "value-" + x)
								 .log("map-log");

		StepVerifier.create(flux)
					.expectNext("value-1", "value-2", "value-3")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("log() propaga gli errori del Flux invariati")
	void testLogPropagatesErrorsUnchanged() {
		Flux<Object> flux = Flux.error(new RuntimeException("boom"))
								 .log();

		StepVerifier.create(flux)
					.expectError(RuntimeException.class)
					.verify();
	}
}
