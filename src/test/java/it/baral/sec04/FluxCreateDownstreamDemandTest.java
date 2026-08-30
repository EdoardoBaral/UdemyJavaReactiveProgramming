package it.baral.sec04;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FluxCreateDownstreamDemand Tests")
class FluxCreateDownstreamDemandTest {

	@Test
	@DisplayName("onRequest() emette esattamente il numero di elementi richiesti dal sottoscrittore")
	void testOnRequestEmitsExactlyRequestedAmount() {
		AtomicInteger counter = new AtomicInteger(0);
		Flux<Integer> flux = Flux.create(sink -> sink.onRequest(request -> {
			for (long i = 0; i < request; i++) {
				sink.next(counter.incrementAndGet());
			}
		}));

		StepVerifier.create(flux, 0)
					.expectSubscription()
					.thenRequest(2)
					.expectNext(1, 2)
					.thenRequest(3)
					.expectNext(3, 4, 5)
					.thenCancel()
					.verify();
	}

	@Test
	@DisplayName("onRequest() non emette nulla prima della prima richiesta")
	void testOnRequestEmitsNothingBeforeFirstRequest() {
		Flux<Integer> flux = Flux.create(sink -> sink.onRequest(request -> {
			for (long i = 0; i < request; i++) {
				sink.next(1);
			}
		}));

		StepVerifier.create(flux, 0)
					.expectSubscription()
					.expectNoEvent(Duration.ofMillis(100))
					.thenCancel()
					.verify();
	}

	@Test
	@DisplayName("onRequest() smette di generare dopo la cancellazione della sottoscrizione")
	void testOnRequestStopsAfterCancellation() {
		AtomicInteger emittedCount = new AtomicInteger(0);
		Flux<Integer> flux = Flux.create(sink -> sink.onRequest(request -> {
			for (long i = 0; i < request && !sink.isCancelled(); i++) {
				emittedCount.incrementAndGet();
				sink.next(1);
			}
		}));

		StepVerifier.create(flux, 0)
					.expectSubscription()
					.thenRequest(2)
					.expectNextCount(2)
					.thenCancel()
					.verify();

		assertEquals(2, emittedCount.get());
	}

	@Test
	@DisplayName("Un create() che emette subito tutti gli elementi ignora la domanda iniziale ma li consegna comunque")
	void testEagerCreateIgnoresInitialDemandButStillDeliversAllElements() {
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
}
