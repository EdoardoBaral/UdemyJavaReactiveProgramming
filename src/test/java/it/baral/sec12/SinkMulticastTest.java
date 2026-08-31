package it.baral.sec12;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("SinkMulticast Tests")
class SinkMulticastTest {

	@Test
	@DisplayName("Un sink multicast recapita gli elementi solo ai subscriber gia' presenti al momento dell'emissione")
	void testMulticastSinkDeliversOnlyToAlreadySubscribedSubscribers() {
		Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
		Flux<String> flux = sink.asFlux();

		List<String> received1 = new ArrayList<>();
		flux.subscribe(received1::add);

		sink.tryEmitNext("a");
		sink.tryEmitNext("b");

		List<String> received2 = new ArrayList<>();
		flux.subscribe(received2::add);

		sink.tryEmitNext("c");

		assertEquals(List.of("a", "b", "c"), received1);
		assertEquals(List.of("c"), received2);
	}

	@Test
	@DisplayName("Tutti i subscriber gia' sottoscritti ricevono le stesse emissioni")
	void testAllCurrentlySubscribedSubscribersReceiveTheSameEmissions() {
		Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
		Flux<String> flux = sink.asFlux();

		List<String> received1 = new ArrayList<>();
		List<String> received2 = new ArrayList<>();
		flux.subscribe(received1::add);
		flux.subscribe(received2::add);

		sink.tryEmitNext("a");
		sink.tryEmitNext("b");

		assertEquals(List.of("a", "b"), received1);
		assertEquals(List.of("a", "b"), received2);
	}
}
