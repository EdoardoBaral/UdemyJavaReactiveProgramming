package it.baral.sec12;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SinkThreadSafety Tests")
class SinkThreadSafetyTest {

	@Test
	@DisplayName("emitNext() con un gestore che ritenta sugli esiti FAIL_NON_SERIALIZED garantisce la consegna di tutti gli elementi anche sotto emissione concorrente")
	void testEmitNextGuaranteesDeliveryOfAllElementsUnderConcurrentEmission() throws InterruptedException {
		int total = 500;
		Sinks.Many<Integer> sink = Sinks.many().unicast().onBackpressureBuffer();
		Flux<Integer> flux = sink.asFlux();
		List<Integer> received = Collections.synchronizedList(new ArrayList<>());
		flux.subscribe(received::add);

		ExecutorService executor = Executors.newFixedThreadPool(8);
		CountDownLatch latch = new CountDownLatch(total);
		for(int i = 0; i < total; i++) {
			int value = i;
			executor.submit(() -> {
				sink.emitNext(value, (signalType, emitResult) -> Sinks.EmitResult.FAIL_NON_SERIALIZED.equals(emitResult));
				latch.countDown();
			});
		}

		assertTrue(latch.await(10, TimeUnit.SECONDS), "Tutte le emissioni devono completare entro il timeout");
		executor.shutdown();

		assertEquals(total, received.size());
	}
}
