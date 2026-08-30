package it.baral.sec03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

@DisplayName("FluxFromStream Tests")
class FluxFromStreamTest {

	@Test
	@DisplayName("fromStream() emette gli elementi dello stream nell'ordine originale")
	void testFromStreamEmitsElementsInOrder() {
		Stream<Integer> stream = List.of(1, 2, 3, 4, 5)
									 .stream();
		Flux<Integer> flux = Flux.fromStream(stream);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("fromStream() riutilizzato su uno stream gia' consumato termina con errore")
	void testFromStreamReusedOnAlreadyConsumedStreamFails() {
		List<Integer> list = List.of(1, 2, 3, 4, 5);
		Stream<Integer> stream = list.stream();

		Flux.fromStream(stream)
			.subscribe();

		Flux<Integer> reusedFlux = Flux.fromStream(stream);

		StepVerifier.create(reusedFlux)
					.expectError(IllegalStateException.class)
					.verify();
	}

	@Test
	@DisplayName("fromStream() con un nuovo stream per ogni sottoscrizione funziona correttamente ad ogni volta")
	void testFromStreamWithFreshStreamEachTimeWorks() {
		List<Integer> list = List.of(1, 2, 3, 4, 5);

		StepVerifier.create(Flux.fromStream(list.stream()))
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();

		StepVerifier.create(Flux.fromStream(list.stream()))
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();
	}
}
