package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ConcatWith Tests")
class ConcatWithTest {

	@Test
	@DisplayName("concatWithValues() antepone gli elementi della sorgente e accoda i valori indicati")
	void testConcatWithValuesAppendsValuesAfterSourceSequence() {
		StepVerifier.create(Flux.just(1, 2, 3).concatWithValues(-1, 0))
					.expectNext(1)
					.expectNextCount(4)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("concatWith() accoda l'intera sequenza del Flux indicato dopo il completamento della sorgente")
	void testConcatWithPublisherAppendsEntireSequenceAfterSource() {
		StepVerifier.create(Flux.just(1, 2, 3).concatWith(Flux.just(4, 5, 6)))
					.expectNext(1)
					.expectNextCount(5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Concatenazioni multiple in sequenza vengono applicate nell'ordine di dichiarazione")
	void testMultipleConcatWithAppliesInDeclarationOrder() {
		List<Integer> result = Flux.just(1, 2, 3)
									.concatWith(Flux.just(4, 5, 6))
									.concatWithValues(1000)
									.collectList()
									.block();

		assertEquals(List.of(1, 2, 3, 4, 5, 6, 1000), result);
	}

	@Test
	@DisplayName("Flux.concat() e' equivalente all'operatore d'istanza concatWith()")
	void testFluxConcatStaticMethodIsEquivalentToConcatWith() {
		StepVerifier.create(Flux.concat(Flux.just(1, 2, 3), Flux.just(4, 5, 6)))
					.expectNext(1)
					.expectNextCount(5)
					.expectComplete()
					.verify();
	}
}
