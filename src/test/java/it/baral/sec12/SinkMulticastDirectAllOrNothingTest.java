package it.baral.sec12;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SinkMulticastDirectAllOrNothing Tests")
class SinkMulticastDirectAllOrNothingTest {

	@Test
	@DisplayName("directAllOrNothing() fa fallire l'emissione per tutti i subscriber non appena uno solo di essi non ha domanda sufficiente")
	void testEmissionFailsForAllSubscribersWhenOneLacksDemand() {
		Sinks.Many<Integer> sink = Sinks.many().multicast().directAllOrNothing();
		Flux<Integer> flux = sink.asFlux();

		List<Integer> fastReceived = new ArrayList<>();
		List<Integer> slowReceived = new ArrayList<>();

		flux.subscribe(fastReceived::add);
		flux.subscribe(new BaseSubscriber<Integer>() {
			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				request(1);
			}

			@Override
			protected void hookOnNext(Integer value) {
				slowReceived.add(value);
			}
		});

		Sinks.EmitResult firstResult = sink.tryEmitNext(1);
		Sinks.EmitResult secondResult = sink.tryEmitNext(2);

		assertEquals(Sinks.EmitResult.OK, firstResult);
		assertTrue(secondResult.isFailure());
		assertEquals(List.of(1), fastReceived);
		assertEquals(List.of(1), slowReceived);
	}
}
