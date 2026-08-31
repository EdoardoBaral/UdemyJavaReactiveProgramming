package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("StartWithUseCase Tests")
class StartWithUseCaseTest {

	@Test
	@DisplayName("Un secondo sottoscrittore riceve dalla cache (via startWith) gli stessi valori gia' generati per il primo, senza generarne di nuovi")
	void testSecondSubscriberReceivesCachedValuesWithoutGeneratingNewOnes() {
		List<String> cache = new ArrayList<>();
		AtomicInteger invocationCount = new AtomicInteger();
		Flux<String> flux = Flux.<String>generate(sink -> {
										String name = "name-" +invocationCount.incrementAndGet();
										cache.add(name);
										sink.next(name);
									})
								 .startWith(cache);

		List<String> received1 = new ArrayList<>();
		flux.take(2)
			.subscribe(received1::add);

		List<String> received2 = new ArrayList<>();
		flux.take(2)
			.subscribe(received2::add);

		assertEquals(List.of("name-1", "name-2"), received1);
		assertEquals(List.of("name-1", "name-2"), received2);
		assertEquals(2, invocationCount.get());
	}

	@Test
	@DisplayName("Un sottoscrittore che richiede piu' valori di quelli in cache riceve prima i valori in cache e poi nuovi valori generati")
	void testSubscriberRequestingMoreThanCachedReceivesCachedValuesThenNewOnes() {
		List<String> cache = new ArrayList<>();
		AtomicInteger invocationCount = new AtomicInteger();
		Flux<String> flux = Flux.<String>generate(sink -> {
										String name = "name-" +invocationCount.incrementAndGet();
										cache.add(name);
										sink.next(name);
									})
								 .startWith(cache);

		flux.take(2).subscribe();

		List<String> received = new ArrayList<>();
		flux.take(3)
			.subscribe(received::add);

		assertEquals(List.of("name-1", "name-2", "name-3"), received);
		assertEquals(3, invocationCount.get());
	}
}
