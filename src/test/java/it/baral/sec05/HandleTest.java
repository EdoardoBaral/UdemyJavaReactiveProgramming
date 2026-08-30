package it.baral.sec05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

@DisplayName("Handle Tests")
class HandleTest {

	@Test
	@DisplayName("handle() trasforma un elemento sostituendone il valore")
	void testHandleTransformsElementByReplacingItsValue() {
		Flux<Integer> flux = Flux.just(1, 2, 3)
								 .handle((item, sink) -> sink.next(item == 1 ? -2 : item));

		StepVerifier.create(flux)
					.expectNext(-2, 2, 3)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("handle() scarta un elemento quando il sink non viene invocato")
	void testHandleDiscardsElementWhenSinkIsNotInvoked() {
		Flux<Integer> flux = Flux.just(1, 2, 3, 4)
								 .handle((item, sink) -> {
									  if (item != 3) {
										  sink.next(item);
									  }
								  });

		StepVerifier.create(flux)
					.expectNext(1, 2, 4)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("handle() fa fallire il Flux quando invoca sink.error()")
	void testHandleFailsFluxWhenSinkErrorIsInvoked() {
		Flux<Integer> flux = Flux.just(1, 2, 3)
								 .handle((item, sink) -> {
									  if (item == 2) {
										  sink.error(new IllegalStateException("ooops"));
									  } else {
										  sink.next(item);
									  }
								  });

		StepVerifier.create(flux)
					.expectNext(1)
					.expectError(IllegalStateException.class)
					.verify();
	}

	@Test
	@DisplayName("handle() puo' completare il Flux anticipatamente in base al valore emesso")
	void testHandleCanCompleteFluxEarlyBasedOnEmittedValue() {
		AtomicInteger counter = new AtomicInteger(0);
		Flux<Integer> flux = Flux.<Integer>generate(sink -> sink.next(counter.incrementAndGet()))
								 .handle((item, sink) -> {
									  sink.next(item);
									  if (item == 3) {
										  sink.complete();
									  }
								  });

		StepVerifier.create(flux)
					.expectNext(1, 2, 3)
					.expectComplete()
					.verify();
	}
}
