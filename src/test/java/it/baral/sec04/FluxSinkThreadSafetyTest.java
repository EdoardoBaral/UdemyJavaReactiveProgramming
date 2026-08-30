package it.baral.sec04;

import it.baral.sec04.helper.NameGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("FluxSinkThreadSafety Tests")
class FluxSinkThreadSafetyTest {

	@Test
	@DisplayName("FluxSink serializza le emissioni concorrenti da piu' thread senza perdere elementi")
	void testFluxSinkSerializesConcurrentEmissionsWithoutLosingElements() throws InterruptedException {
		int threadCount = 10;
		int emissionsPerThread = 1000;
		NameGenerator nameGenerator = new NameGenerator();
		Flux<String> flux = Flux.create(nameGenerator);
		List<String> received = new ArrayList<>();
		flux.subscribe(received::add);

		CountDownLatch latch = new CountDownLatch(threadCount);
		Runnable task = () -> {
			for (int i = 0; i < emissionsPerThread; i++) {
				nameGenerator.generate();
			}
			latch.countDown();
		};

		for (int i = 0; i < threadCount; i++) {
			Thread.ofPlatform().start(task);
		}

		assertTrue(latch.await(10, TimeUnit.SECONDS), "Tutti i thread devono completare l'emissione entro il timeout");
		assertEquals(threadCount * emissionsPerThread, received.size(), "Nessun elemento deve andare perso grazie alla serializzazione del FluxSink");
	}

	@Test
	@DisplayName("Un singolo thread che genera valori sul FluxSink non perde elementi")
	void testSingleThreadGeneratingValuesDoesNotLoseElements() {
		NameGenerator nameGenerator = new NameGenerator();
		Flux<String> flux = Flux.create(nameGenerator);
		List<String> received = new ArrayList<>();
		flux.subscribe(received::add);

		for (int i = 0; i < 100; i++) {
			nameGenerator.generate();
		}

		assertEquals(100, received.size());
	}
}
