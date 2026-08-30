package it.baral.sec06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ColdPublisher Tests")
class ColdPublisherTest {

	@Test
	@DisplayName("Un Flux cold emette la sequenza di valori generata dalla propria sorgente")
	void testColdFluxEmitsGeneratedSequence() {
		Flux<Integer> flux = coldFlux(new AtomicInteger());

		StepVerifier.create(flux)
					.expectNext(1, 2, 3)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Ogni sottoscrizione a un Flux cold innesca una nuova esecuzione della sorgente, senza resettare lo stato esterno condiviso")
	void testEachSubscriptionTriggersNewSourceExecutionWithoutResettingSharedState() {
		AtomicInteger atomicInteger = new AtomicInteger();
		Flux<Integer> flux = coldFlux(atomicInteger);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3)
					.expectComplete()
					.verify();

		StepVerifier.create(flux)
					.expectNext(4, 5, 6)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Il numero di invocazioni della sorgente cresce di uno per ogni nuova sottoscrizione")
	void testSourceInvocationCountIncreasesByOnePerNewSubscription() {
		AtomicInteger invocationCount = new AtomicInteger();
		Flux<Integer> flux = Flux.create(sink -> {
								  			invocationCount.incrementAndGet();
								  			sink.next(1);
								  			sink.complete();
								  		});

		flux.subscribe();
		flux.subscribe();
		flux.subscribe();

		assertEquals(3, invocationCount.get());
	}

	private Flux<Integer> coldFlux(AtomicInteger atomicInteger) {
		return Flux.create(sink -> {
							  for(int i=0; i<3; i++) {
							  	sink.next(atomicInteger.incrementAndGet());
							  }
							  sink.complete();
						  });
	}
}
