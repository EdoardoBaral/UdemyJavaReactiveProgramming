package it.baral.sec12;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("SinkMulticastReplay Tests")
class SinkMulticastReplayTest {

	@Test
	@DisplayName("replay().all() consegna a un subscriber tardivo tutti gli elementi gia' emessi in precedenza")
	void testReplayAllDeliversAllPreviouslyEmittedValuesToLateSubscriber() {
		Sinks.Many<String> sink = Sinks.many().replay().all();
		Flux<String> flux = sink.asFlux();

		sink.tryEmitNext("a");
		sink.tryEmitNext("b");

		List<String> received = new ArrayList<>();
		flux.subscribe(received::add);

		sink.tryEmitNext("c");

		assertEquals(List.of("a", "b", "c"), received);
	}

	@Test
	@DisplayName("replay().all() consegna la stessa cronologia a piu' subscriber tardivi")
	void testReplayAllDeliversSameHistoryToMultipleLateSubscribers() {
		Sinks.Many<String> sink = Sinks.many().replay().all();
		Flux<String> flux = sink.asFlux();

		sink.tryEmitNext("a");
		sink.tryEmitNext("b");

		List<String> received1 = new ArrayList<>();
		List<String> received2 = new ArrayList<>();
		flux.subscribe(received1::add);
		flux.subscribe(received2::add);

		assertEquals(List.of("a", "b"), received1);
		assertEquals(List.of("a", "b"), received2);
	}
}
