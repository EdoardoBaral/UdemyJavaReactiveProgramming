package it.baral.sec08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("MultipleSubscribers Tests")
class MultipleSubscribersTest {

	@Test
	@DisplayName("Ogni sottoscrittore a un Flux cold innesca una propria esecuzione indipendente della sorgente")
	void testEachSubscriberTriggersIndependentSourceExecution() {
		AtomicInteger invocationCount = new AtomicInteger();
		Flux<Integer> producer = Flux.generate(() -> 1, (state, sink) -> {
										  			invocationCount.incrementAndGet();
										  			sink.next(state);
										  			return state +1;
										  		  });

		List<Integer> received1 = new ArrayList<>();
		producer.take(5)
				.subscribe(received1::add);

		List<Integer> received2 = new ArrayList<>();
		producer.take(3)
				.subscribe(received2::add);

		assertEquals(List.of(1, 2, 3, 4, 5), received1);
		assertEquals(List.of(1, 2, 3), received2);
		assertEquals(8, invocationCount.get());
	}

	@Test
	@DisplayName("Sottoscrittori indipendenti possono richiedere a velocita' diverse senza influenzarsi a vicenda")
	void testSubscribersCanRequestAtIndependentRates() {
		Flux<Integer> flux = Flux.range(1, 5);

		StepVerifier.create(flux, 0)
					.expectSubscription()
					.thenRequest(2)
					.expectNext(1, 2)
					.thenCancel()
					.verify();

		StepVerifier.create(flux)
					.expectNext(1)
					.expectNextCount(4)
					.expectComplete()
					.verify();
	}
}
