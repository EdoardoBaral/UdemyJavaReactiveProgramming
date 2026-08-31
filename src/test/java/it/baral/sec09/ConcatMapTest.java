package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ConcatMap Tests")
class ConcatMapTest {

	@Test
	@DisplayName("concatMap() preserva l'ordine di emissione della sorgente originale")
	void testConcatMapPreservesSourceEmissionOrder() {
		StepVerifier.create(Flux.range(1, 3).concatMap(i -> Flux.just("v" +i)))
					.expectNext("v1", "v2", "v3")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("concatMap() elabora le sorgenti generate in sequenza, sottoscrivendo la successiva solo dopo il completamento della precedente")
	void testConcatMapProcessesSourcesSequentiallyNotConcurrently() {
		List<String> events = Collections.synchronizedList(new ArrayList<>());
		Flux<Integer> flux = Flux.range(1, 3)
								  .concatMap(i -> Flux.just(i)
													   .delayElements(Duration.ofMillis(10))
													   .doOnSubscribe(s -> events.add("subscribe-" +i))
													   .doOnComplete(() -> events.add("complete-" +i)));

		StepVerifier.create(flux)
					.expectNext(1, 2, 3)
					.expectComplete()
					.verify();

		assertEquals(List.of("subscribe-1", "complete-1", "subscribe-2", "complete-2", "subscribe-3", "complete-3"), events);
	}
}
