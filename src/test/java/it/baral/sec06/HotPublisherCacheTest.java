package it.baral.sec06;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("HotPublisherCache Tests")
class HotPublisherCacheTest {

	@Test
	@DisplayName("stockStream() emette un nuovo prezzo ogni 3 secondi, senza mai completare")
	void testStockStreamEmitsNewPriceEveryThreeSecondsWithoutCompleting() {
		StepVerifier.withVirtualTime(this::stockStream)
					.expectSubscription()
					.expectNoEvent(Duration.ofSeconds(3))
					.expectNextCount(1)
					.expectNoEvent(Duration.ofSeconds(3))
					.expectNextCount(1)
					.thenCancel()
					.verify();
	}

	@Test
	@DisplayName("replay(10): un sottoscrittore che si unisce dopo riceve comunque, dalla cache, gli elementi già emessi prima della sua iscrizione")
	void testReplayCachesEmittedElementsForLateSubscribers() {
		AtomicReference<Flux<Integer>> stockFluxRef = new AtomicReference<>();
		List<Integer> received2 = new ArrayList<>();

		StepVerifier.withVirtualTime(() -> {
						Flux<Integer> stockFlux = stockStream().replay(10).autoConnect(0);
						stockFluxRef.set(stockFlux);
						return stockFlux;
					})
					.expectSubscription()
					.expectNoEvent(Duration.ofSeconds(3))
					.expectNextCount(1)
					.expectNoEvent(Duration.ofSeconds(3))
					.expectNextCount(1)
					.then(() -> stockFluxRef.get().subscribe(received2::add))
					.thenAwait(Duration.ofSeconds(3))
					.expectNextCount(1)
					.thenCancel()
					.verify();

		assertEquals(3, received2.size(), "sub2 deve ricevere i 2 elementi già in cache più quello nuovo emesso dopo la sua iscrizione");
	}

	private Flux<Integer> stockStream() {
		return Flux.generate(sink -> sink.next(1))
				   .delayElements(Duration.ofSeconds(3))
				   .cast(Integer.class);
	}
}
