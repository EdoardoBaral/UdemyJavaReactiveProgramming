package it.baral.sec04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DisplayName("TakeOperator Tests")
class TakeOperatorTest {

	@Test
	@DisplayName("take(3) interrompe il flusso dopo i primi 3 elementi emessi")
	void testTakeInterruptsAfterFirstThreeElements() {
		Flux<Integer> flux = Flux.range(1, 10)
								 .take(3);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("take(0) non emette alcun elemento")
	void testTakeWithZeroEmitsNothing() {
		Flux<Integer> flux = Flux.range(1, 10)
								 .take(0);

		StepVerifier.create(flux)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("takeWhile(i -> i < 6) emette gli elementi finche' la condizione e' vera, escludendo quello che la fa fallire")
	void testTakeWhileEmitsElementsWhileConditionIsTrueExcludingFailingElement() {
		Flux<Integer> flux = Flux.range(1, 10)
								 .takeWhile(i -> i < 6);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("takeWhile() con condizione falsa sul primo elemento non emette nulla")
	void testTakeWhileWithConditionFalseOnFirstElementEmitsNothing() {
		Flux<Integer> flux = Flux.range(1, 10)
								 .takeWhile(i -> i > 100);

		StepVerifier.create(flux)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("takeUntil(i -> i == 6) emette gli elementi finche' la condizione non si verifica, includendo quello che la soddisfa")
	void testTakeUntilEmitsElementsUntilConditionIsMetIncludingSatisfyingElement() {
		Flux<Integer> flux = Flux.range(1, 10)
								 .takeUntil(i -> i == 6);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5, 6)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("takeUntil() con condizione vera sul primo elemento emette solo quell'elemento")
	void testTakeUntilWithConditionTrueOnFirstElementEmitsOnlyThatElement() {
		Flux<Integer> flux = Flux.range(1, 10)
								 .takeUntil(i -> i == 1);

		StepVerifier.create(flux)
					.expectNext(1)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("takeWhile() e takeUntil() con la stessa soglia producono risultati diversi sullo stesso Flux sorgente")
	void testTakeWhileAndTakeUntilWithSameThresholdProduceDifferentResults() {
		Flux<Integer> source = Flux.range(1, 10);

		StepVerifier.create(source.takeWhile(i -> i < 6))
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();

		StepVerifier.create(source.takeUntil(i -> i == 6))
					.expectNext(1, 2, 3, 4, 5, 6)
					.expectComplete()
					.verify();
	}
}
