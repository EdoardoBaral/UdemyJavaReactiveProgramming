package it.baral.sec04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

@DisplayName("FluxGenerateUntil Tests")
class FluxGenerateUntilTest {

	@Test
	@DisplayName("Terminare internamente al generatore ferma il Flux non appena la condizione e' soddisfatta")
	void testCompletingInsideGeneratorStopsAsSoonAsConditionIsMet() {
		AtomicInteger counter = new AtomicInteger(0);
		Flux<Integer> flux = Flux.generate(sink -> {
			int value = counter.incrementAndGet();
			sink.next(value);
			if (value == 5) {
				sink.complete();
			}
		});

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Un generatore che non completa mai continua a essere richiamato senza takeUntil()")
	void testGeneratorThatNeverCompletesKeepsBeingInvokedWithoutTakeUntil() {
		AtomicInteger counter = new AtomicInteger(0);
		Flux<Integer> flux = Flux.generate(sink -> sink.next(counter.incrementAndGet()));

		StepVerifier.create(flux.take(5))
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("takeUntil() applicato a valle termina il Flux non appena la condizione diventa vera, includendo l'elemento")
	void testTakeUntilTerminatesDownstreamAsSoonAsConditionBecomesTrueIncludingMatchingElement() {
		AtomicInteger counter = new AtomicInteger(0);
		Flux<Integer> flux = Flux.<Integer>generate(sink -> sink.next(counter.incrementAndGet()))
								 .takeUntil(value -> value == 5);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();
	}
}
