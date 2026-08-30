package it.baral.sec04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@DisplayName("FluxGenerateWithState Tests")
class FluxGenerateWithStateTest {

	@Test
	@DisplayName("Lo stato iniziale viene passato al generatore alla prima invocazione")
	void testInitialStateIsPassedToGeneratorOnFirstInvocation() {
		Flux<Integer> flux = Flux.generate(() -> 0, (counter, sink) -> {
			sink.next(counter);
			int next = counter + 1;
			if (next == 3) {
				sink.complete();
			}
			return next;
		});

		StepVerifier.create(flux)
					.expectNext(0, 1, 2)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Il conteggio di elementi prodotti termina il Flux quando raggiunge il limite, a parita' di condizione sul valore mai soddisfatta")
	void testElementCountTerminatesFluxWhenLimitReachedAndValueConditionNeverMet() {
		Flux<Integer> flux = Flux.generate(() -> 0, (counter, sink) -> {
			sink.next(-1);
			int nextCounter = counter + 1;
			if (nextCounter == 10) {
				sink.complete();
			}
			return nextCounter;
		});

		StepVerifier.create(flux)
					.expectNextCount(10)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("La condizione sul valore emesso termina il Flux prima che il conteggio raggiunga il limite")
	void testValueConditionTerminatesFluxBeforeCountReachesLimit() {
		Flux<Integer> flux = Flux.generate(() -> 0, (counter, sink) -> {
			int value = counter;
			sink.next(value);
			int nextCounter = counter + 1;
			if (value == 3 || nextCounter == 10) {
				sink.complete();
			}
			return nextCounter;
		});

		StepVerifier.create(flux)
					.expectNext(0, 1, 2, 3)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Lo stato aggiornato viene propagato correttamente tra invocazioni successive del generatore")
	void testUpdatedStateIsPropagatedCorrectlyAcrossSuccessiveGeneratorInvocations() {
		Flux<Integer> flux = Flux.generate(() -> 10, (counter, sink) -> {
			sink.next(counter);
			int next = counter + 10;
			if (counter == 30) {
				sink.complete();
			}
			return next;
		});

		StepVerifier.create(flux)
					.expectNext(10, 20, 30)
					.expectComplete()
					.verify();
	}
}
