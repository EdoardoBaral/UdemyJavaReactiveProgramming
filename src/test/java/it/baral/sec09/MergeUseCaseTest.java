package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MergeUseCase Tests")
class MergeUseCaseTest {

	@Test
	@DisplayName("Flux.merge() aggrega in un unico flusso i risultati di piu' sorgenti concorrenti")
	void testMergeAggregatesResultsFromMultipleConcurrentSources() {
		List<String> result = Flux.merge(Flux.just("emirates-1", "emirates-2"),
										  Flux.just("qatar-1"),
										  Flux.just("american-1", "american-2", "american-3"))
								   .collectList()
								   .block();

		assertEquals(6, result.size());
		assertTrue(result.containsAll(List.of("emirates-1", "emirates-2", "qatar-1", "american-1", "american-2", "american-3")));
	}

	@Test
	@DisplayName("take(Duration) interrompe la raccolta dei risultati trascorso il tempo indicato, indipendentemente da ulteriori emissioni")
	void testTakeWithDurationStopsCollectingAfterGivenDuration() {
		StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofMillis(300))
												.map(i -> "flight-" +i)
												.take(Duration.ofSeconds(1)))
					.thenAwait(Duration.ofSeconds(1))
					.expectNextCount(3)
					.expectComplete()
					.verify();
	}
}
