package it.baral.sec05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Transform Tests")
class TransformTest {

	@Test
	@DisplayName("transform() applica l'operatore fornito quando la condizione e' vera")
	void testTransformAppliesGivenOperatorWhenConditionIsTrue() {
		UnaryOperator<Flux<Integer>> doubleValues = flux -> flux.map(i -> i * 2);
		boolean isEnabled = true;

		Flux<Integer> flux = Flux.range(1, 3)
								 .transform(isEnabled ? doubleValues : Function.identity());

		StepVerifier.create(flux)
					.expectNext(2, 4, 6)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("transform() lascia il Flux invariato quando la condizione e' falsa")
	void testTransformLeavesFluxUnchangedWhenConditionIsFalse() {
		UnaryOperator<Flux<Integer>> doubleValues = flux -> flux.map(i -> i * 2);
		boolean isEnabled = false;

		Flux<Integer> flux = Flux.range(1, 3)
								 .transform(isEnabled ? doubleValues : Function.identity());

		StepVerifier.create(flux)
					.expectNext(1, 2, 3)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("transform() puo' operare su un Flux di record definiti dalla classe sotto test")
	void testTransformCanOperateOnFluxOfRecordsDefinedByClassUnderTest() {
		UnaryOperator<Flux<Transform.Customer>> upperCaseNames = flux -> flux.map(
				customer -> new Transform.Customer(customer.id(), customer.name().toUpperCase()));

		Flux<Transform.Customer> flux = Flux.just(new Transform.Customer(1, "alice"), new Transform.Customer(2, "bob"))
											.transform(upperCaseNames);

		StepVerifier.create(flux)
					.expectNext(new Transform.Customer(1, "ALICE"), new Transform.Customer(2, "BOB"))
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("transform() costruisce la trasformazione una sola volta, al momento della composizione")
	void testTransformBuildsTransformationOnlyOnceAtCompositionTime() {
		AtomicInteger compositionCount = new AtomicInteger(0);
		Function<Flux<Integer>, Flux<Integer>> countingOperator = flux -> {
			compositionCount.incrementAndGet();
			return flux.map(i -> i + 1);
		};

		Flux<Integer> flux = Flux.range(1, 3)
								 .transform(countingOperator);

		assertEquals(1, compositionCount.get(), "transform() deve costruire la trasformazione subito, non alla sottoscrizione");

		StepVerifier.create(flux)
					.expectNext(2, 3, 4)
					.expectComplete()
					.verify();

		assertEquals(1, compositionCount.get(), "transform() non deve ricostruire la trasformazione ad ogni sottoscrizione");
	}
}
