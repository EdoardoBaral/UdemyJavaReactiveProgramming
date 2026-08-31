package it.baral.sec10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("GroupBy Tests")
class GroupByTest {

	@Test
	@DisplayName("groupBy() suddivide gli elementi in sotto-flussi in base alla chiave di classificazione")
	void testGroupByPartitionsElementsByClassificationKey() {
		Map<Boolean, List<Integer>> groups = Flux.range(1, 10)
												  .groupBy(i -> i % 2 == 0)
												  .flatMap(g -> g.collectList().map(list -> Map.entry(g.key(), list)))
												  .collectMap(Map.Entry::getKey, Map.Entry::getValue)
												  .block();

		assertEquals(List.of(2, 4, 6, 8, 10), groups.get(true));
		assertEquals(List.of(1, 3, 5, 7, 9), groups.get(false));
	}

	@Test
	@DisplayName("Ogni GroupedFlux espone la propria chiave di classificazione tramite key()")
	void testGroupedFluxExposesItsClassificationKey() {
		List<Boolean> keys = Flux.range(1, 4)
								  .groupBy(i -> i % 2 == 0)
								  .map(GroupedFlux::key)
								  .collectList()
								  .block();

		assertEquals(2, keys.size());
		assertTrue(keys.containsAll(List.of(true, false)));
	}

	@Test
	@DisplayName("Quando la funzione di classificazione produce sempre la stessa chiave, groupBy() genera un unico gruppo")
	void testGroupByProducesSingleGroupWhenAllKeysAreEqual() {
		List<GroupedFlux<Boolean, Integer>> groups = Flux.range(1, 5)
														  .map(i -> i *2)
														  .groupBy(i -> i % 2 == 0)
														  .collectList()
														  .block();

		assertEquals(1, groups.size());
		assertEquals(true, groups.get(0).key());
	}
}
