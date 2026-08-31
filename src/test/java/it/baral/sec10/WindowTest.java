package it.baral.sec10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Window Tests")
class WindowTest {

	@Test
	@DisplayName("window(n) suddivide la sorgente in sotto-flussi (finestre) della dimensione fissa indicata")
	void testWindowSplitsSourceIntoFixedSizeSubFluxes() {
		List<List<Integer>> windows = Flux.range(1, 7)
										   .window(3)
										   .flatMap(Flux::collectList)
										   .collectList()
										   .block();

		assertEquals(List.of(List.of(1, 2, 3), List.of(4, 5, 6), List.of(7)), windows);
	}

	@Test
	@DisplayName("Ogni finestra puo' essere elaborata reattivamente in modo indipendente, preservando il numero totale di elementi")
	void testEachWindowCanBeProcessedIndependentlyPreservingTotalElementCount() {
		AtomicInteger totalProcessed = new AtomicInteger();
		AtomicInteger windowCount = new AtomicInteger();

		Flux.range(1, 10)
			.window(4)
			.flatMap(w -> {
					windowCount.incrementAndGet();
					return w.doOnNext(e -> totalProcessed.incrementAndGet()).then();
				})
			.blockLast();

		assertEquals(10, totalProcessed.get());
		assertEquals(3, windowCount.get());
	}
}
