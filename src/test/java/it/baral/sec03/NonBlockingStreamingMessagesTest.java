package it.baral.sec03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("NonBlockingStreamingMessages Tests")
class NonBlockingStreamingMessagesTest {

	@Test
	@DisplayName("Due sottoscrizioni indipendenti allo stesso stream ricevono ciascuna l'intera sequenza")
	void testTwoIndependentSubscriptionsEachReceiveFullStream() {
		Flux<String> stream = Flux.just("a", "b", "c");
		List<String> sub1Results = new ArrayList<>();
		List<String> sub2Results = new ArrayList<>();

		stream.subscribe(sub1Results::add);
		stream.subscribe(sub2Results::add);

		assertEquals(List.of("a", "b", "c"), sub1Results);
		assertEquals(List.of("a", "b", "c"), sub2Results);
	}

	@Test
	@DisplayName("subscribe() sullo stream non blocca il thread chiamante")
	void testSubscribeToStreamDoesNotBlockCallingThread() {
		Flux<String> stream = Flux.just("x", "y", "z");

		long start = System.currentTimeMillis();
		stream.subscribe(value -> {});
		long elapsed = System.currentTimeMillis() - start;

		assertTrue(elapsed < 100, "subscribe() deve ritornare immediatamente senza attendere il completamento dello stream");
	}

	@Test
	@DisplayName("Sottoscrizioni multiple e concorrenti allo stesso stream restano indipendenti tra loro")
	void testMultipleConcurrentSubscriptionsRemainIndependent() {
		Flux<Integer> stream = Flux.range(1, 5);
		AtomicInteger sub1Count = new AtomicInteger(0);
		AtomicInteger sub2Count = new AtomicInteger(0);

		stream.subscribe(value -> sub1Count.incrementAndGet());
		stream.subscribe(value -> sub2Count.incrementAndGet());

		assertEquals(5, sub1Count.get());
		assertEquals(5, sub2Count.get());
	}
}
