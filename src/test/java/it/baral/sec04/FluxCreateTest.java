package it.baral.sec04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FluxCreate Tests")
class FluxCreateTest {

	@Test
	@DisplayName("create() emette i valori passati esplicitamente al FluxSink e poi completa")
	void testCreateEmitsExplicitValuesAndCompletes() {
		Flux<Integer> flux = Flux.create(sink -> {
			sink.next(1);
			sink.next(2);
			sink.complete();
		});

		StepVerifier.create(flux)
					.expectNext(1, 2)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("create() con un ciclo for emette un elemento per ogni iterazione")
	void testCreateWithForLoopEmitsOneElementPerIteration() {
		Flux<Integer> flux = Flux.create(sink -> {
			for (int i = 0; i < 10; i++) {
				sink.next(i);
			}
			sink.complete();
		});

		StepVerifier.create(flux)
					.expectNextCount(10)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("create() con un ciclo do-while termina non appena la condizione e' soddisfatta")
	void testCreateWithDoWhileLoopTerminatesWhenConditionIsMet() {
		AtomicInteger counter = new AtomicInteger(0);
		Flux<Integer> flux = Flux.create(sink -> {
			int value;
			do {
				value = counter.incrementAndGet();
				sink.next(value);
			} while (value < 5);
			sink.complete();
		});

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("create() propaga un errore emesso esplicitamente sul FluxSink")
	void testCreatePropagatesExplicitError() {
		Flux<Integer> flux = Flux.create(sink -> {
			sink.next(1);
			sink.error(new RuntimeException("sink error"));
		});

		StepVerifier.create(flux)
					.expectNext(1)
					.expectError(RuntimeException.class)
					.verify();
	}

	@Test
	@DisplayName("create() invoca il consumer per ogni sottoscrizione")
	void testCreateInvokesConsumerForEachSubscription() {
		AtomicInteger invocationCount = new AtomicInteger(0);
		Flux<Integer> flux = Flux.create(sink -> {
			invocationCount.incrementAndGet();
			sink.next(1);
			sink.complete();
		});

		StepVerifier.create(flux)
					.expectNext(1)
					.expectComplete()
					.verify();

		assertEquals(1, invocationCount.get());

		StepVerifier.create(flux)
					.expectNext(1)
					.expectComplete()
					.verify();

		assertEquals(2, invocationCount.get(), "Il consumer deve essere invocato per ogni sottoscrizione");
	}
}
