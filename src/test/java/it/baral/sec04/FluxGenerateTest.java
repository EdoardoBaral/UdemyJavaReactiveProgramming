package it.baral.sec04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FluxGenerate Tests")
class FluxGenerateTest {

	@Test
	@DisplayName("generate() senza complete() continua a essere richiamato indefinitamente, limitato da take()")
	void testGenerateWithoutCompleteContinuesIndefinitelyUntilLimitedByTake() {
		AtomicInteger invocationCount = new AtomicInteger(0);
		Flux<Integer> flux = Flux.generate(sink -> {
			invocationCount.incrementAndGet();
			sink.next(1);
		});

		StepVerifier.create(flux.take(4))
					.expectNext(1, 1, 1, 1)
					.expectComplete()
					.verify();

		assertEquals(4, invocationCount.get());
	}

	@Test
	@DisplayName("generate() invoca il generatore esattamente una volta per ogni richiesta a valle")
	void testGenerateInvokesGeneratorOncePerDownstreamRequest() {
		AtomicInteger invocationCount = new AtomicInteger(0);
		Flux<Integer> flux = Flux.generate(sink -> {
			invocationCount.incrementAndGet();
			sink.next(1);
		});

		StepVerifier.create(flux, 0)
					.expectSubscription()
					.thenRequest(3)
					.expectNext(1, 1, 1)
					.thenCancel()
					.verify();

		assertEquals(3, invocationCount.get());
	}

	@Test
	@DisplayName("generate() con complete() nella stessa invocazione emette un solo elemento e termina")
	void testGenerateWithCompleteInSameInvocationEmitsOneElementAndTerminates() {
		Flux<Integer> flux = Flux.generate(sink -> {
			sink.next(1);
			sink.complete();
		});

		StepVerifier.create(flux)
					.expectNext(1)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("generate() propaga un errore emesso sul SynchronousSink")
	void testGeneratePropagatesErrorFromSynchronousSink() {
		Flux<Integer> flux = Flux.generate(sink -> sink.error(new RuntimeException("generator error")));

		StepVerifier.create(flux)
					.expectError(RuntimeException.class)
					.verify();
	}
}
