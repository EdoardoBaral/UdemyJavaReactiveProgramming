package it.baral.sec03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DisplayName("FluxJust Tests")
class FluxJustTest {

	@Test
	@DisplayName("just() emette gli elementi forniti nell'ordine indicato")
	void testJustEmitsElementsInOrder() {
		Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("just() con un solo elemento emette quell'elemento e completa")
	void testJustWithSingleElement() {
		Flux<String> flux = Flux.just("unico");

		StepVerifier.create(flux)
					.expectNext("unico")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("just() completa dopo aver emesso tutti gli elementi")
	void testJustCompletesAfterAllElements() {
		Flux<Integer> flux = Flux.just(10, 20);

		StepVerifier.create(flux)
					.expectNextCount(2)
					.expectComplete()
					.verify();
	}
}
