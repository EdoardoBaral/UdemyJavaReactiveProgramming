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

@DisplayName("SinkMulticastDirectBestEffort Tests")
class SinkMulticastDirectBestEffortTest {

	@Test
	@DisplayName("directBestEffort() scarta silenziosamente gli elementi verso un subscriber senza domanda, senza far fallire l'emissione per gli altri")
	void testSlowSubscriberSilentlyMissesValuesWithoutFailingEmissionForOthers() {
		Sinks.Many<Integer> sink = Sinks.many().multicast().directBestEffort();
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
		assertEquals(Sinks.EmitResult.OK, secondResult);
		assertEquals(List.of(1, 2), fastReceived);
		assertEquals(List.of(1), slowReceived);
	}

	@Test
	@DisplayName("Un onBackpressureBuffer() applicato al subscriber lento evita la perdita di elementi anche con directBestEffort()")
	void testOnBackpressureBufferOnSlowSubscriberAvoidsValueLoss() {
		Sinks.Many<Integer> sink = Sinks.many().multicast().directBestEffort();
		Flux<Integer> flux = sink.asFlux();

		List<Integer> fastReceived = new ArrayList<>();
		List<Integer> slowReceived = new ArrayList<>();

		flux.subscribe(fastReceived::add);
		flux.onBackpressureBuffer()
			.subscribe(new BaseSubscriber<Integer>() {
					@Override
					protected void hookOnSubscribe(Subscription subscription) {
						request(1);
					}

					@Override
					protected void hookOnNext(Integer value) {
						slowReceived.add(value);
					}
				});

		for(int i = 1; i <= 5; i++) {
			assertEquals(Sinks.EmitResult.OK, sink.tryEmitNext(i));
		}

		assertEquals(List.of(1, 2, 3, 4, 5), fastReceived);
		assertEquals(List.of(1), slowReceived);
	}
}
