package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CollectList Tests")
class CollectListTest {

	@Test
	@DisplayName("collectList() raccoglie tutti gli elementi del Flux in un'unica List")
	void testCollectListEmitsSingleListContainingAllElements() {
		StepVerifier.create(Flux.range(1, 5).collectList())
					.expectNext(List.of(1, 2, 3, 4, 5))
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("collectList() su un Flux vuoto emette una List vuota")
	void testCollectListOnEmptyFluxEmitsEmptyList() {
		StepVerifier.create(Flux.<Integer>empty().collectList())
					.expectNext(List.of())
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("collectList() propaga un errore emesso dal Flux sorgente")
	void testCollectListPropagatesUpstreamError() {
		Flux<Integer> flux = Flux.concat(Flux.just(1, 2), Flux.error(new RuntimeException("boom")));

		StepVerifier.create(flux.collectList())
					.expectError(RuntimeException.class)
					.verify();
	}

	@Test
	@DisplayName("collectList() applicato dopo flatMap raccoglie tutti i risultati appiattiti, indipendentemente dall'ordine di arrivo")
	void testCollectListAggregatesResultsFromFlatMappedSources() {
		List<Integer> result = Flux.range(1, 3)
									.flatMap(i -> Flux.just(i *10, i *10 +1))
									.collectList()
									.block();

		assertEquals(6, result.size());
		assertTrue(result.containsAll(List.of(10, 11, 20, 21, 30, 31)));
	}
}
