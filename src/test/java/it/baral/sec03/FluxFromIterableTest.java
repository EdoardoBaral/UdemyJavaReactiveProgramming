package it.baral.sec03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

@DisplayName("FluxFromIterable Tests")
class FluxFromIterableTest {

	@Test
	@DisplayName("fromIterable() emette gli elementi della lista nell'ordine originale")
	void testFromIterableEmitsElementsInOrder() {
		List<String> list = List.of("A", "B", "C", "D", "E");
		Flux<String> flux = Flux.fromIterable(list);

		StepVerifier.create(flux)
					.expectNext("A", "B", "C", "D", "E")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("fromIterable() con lista vuota completa senza emettere elementi")
	void testFromIterableWithEmptyList() {
		Flux<String> flux = Flux.fromIterable(List.of());

		StepVerifier.create(flux)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("fromArray() emette gli elementi dell'array nell'ordine originale")
	void testFromArrayEmitsElementsInOrder() {
		Integer[] array = {1, 2, 3, 4, 5};
		Flux<Integer> flux = Flux.fromArray(array);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("fromArray() con array vuoto completa senza emettere elementi")
	void testFromArrayWithEmptyArray() {
		Integer[] array = {};
		Flux<Integer> flux = Flux.fromArray(array);

		StepVerifier.create(flux)
					.expectComplete()
					.verify();
	}
}
