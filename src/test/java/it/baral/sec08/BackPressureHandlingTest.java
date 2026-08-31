package it.baral.sec08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("BackPressureHandling Tests")
class BackPressureHandlingTest {

	@Test
	@DisplayName("Il meccanismo di richiesta regola automaticamente il produttore: il generatore viene invocato solo per la quantita' richiesta")
	void testGeneratorIsInvokedOnlyAsManyTimesAsRequested() {
		AtomicInteger invocationCount = new AtomicInteger();
		Flux<Integer> producer = Flux.generate(() -> 1, (state, sink) -> {
										  		        	invocationCount.incrementAndGet();
										  		        	sink.next(state);
										  		        	return state +1;
											            });

		StepVerifier.create(producer, 0)
					.expectSubscription()
					.thenRequest(3)
					.expectNext(1, 2, 3)
					.thenCancel()
					.verify();

		assertEquals(3, invocationCount.get());
	}

	@Test
	@DisplayName("take() limita il numero di invocazioni del generatore stateful alla quantita' effettivamente consumata")
	void testGeneratorInvocationCountIsLimitedByTake() {
		AtomicInteger invocationCount = new AtomicInteger();
		Flux<Integer> producer = Flux.generate(() -> 1, (state, sink) -> {
										  		        	invocationCount.incrementAndGet();
										  		        	sink.next(state);
										  		        	return state +1;
											            });

		StepVerifier.create(producer.take(5))
					.expectNext(1)
					.expectNextCount(4)
					.expectComplete()
					.verify();

		assertEquals(5, invocationCount.get());
	}
}
