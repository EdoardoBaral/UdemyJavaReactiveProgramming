package it.baral.sec03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DisplayName("MultipleSubscribers Tests")
class MultipleSubscribersTest {

	@Test
	@DisplayName("Un Flux cold riemette l'intera sequenza per ogni nuovo subscriber")
	void testColdFluxReemitsFullSequenceForEachSubscriber() {
		Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("filter() applicato a una sottoscrizione non influenza le altre sottoscrizioni sullo stesso Flux")
	void testFilterOnOneSubscriptionDoesNotAffectOthers() {
		Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();

		StepVerifier.create(flux.filter(e -> e > 7))
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("filter() e map() applicati indipendentemente producono risultati diversi sullo stesso Flux sorgente")
	void testFilterAndMapAppliedIndependentlyProduceDifferentResults() {
		Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);

		StepVerifier.create(flux.filter(e -> e % 2 == 0))
					.expectNext(2, 4)
					.expectComplete()
					.verify();

		StepVerifier.create(flux.filter(e -> e % 2 == 0).map(e -> e + "a"))
					.expectNext("2a", "4a")
					.expectComplete()
					.verify();
	}
}
