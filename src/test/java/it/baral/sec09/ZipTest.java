package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Zip Tests")
class ZipTest {

	@Test
	@DisplayName("Flux.zip() combina l'ennesimo elemento di ciascuna sorgente in un'unica tupla")
	void testZipCombinesNthElementsFromEachSourceIntoTuples() {
		StepVerifier.create(Flux.zip(Flux.just("a", "b", "c"), Flux.just(1, 2, 3)))
					.expectNextMatches(t -> t.getT1().equals("a") && t.getT2().equals(1))
					.expectNextMatches(t -> t.getT1().equals("b") && t.getT2().equals(2))
					.expectNextMatches(t -> t.getT1().equals("c") && t.getT2().equals(3))
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Flux.zip() termina quando la sorgente piu' corta si esaurisce, anche se le altre hanno ancora elementi")
	void testZipTerminatesWhenShortestSourceCompletes() {
		List<Tuple2<String, Integer>> result = Flux.zip(Flux.just("a", "b", "c", "d", "e"), Flux.just(1, 2, 3))
													.collectList()
													.block();

		assertEquals(3, result.size());
	}

	@Test
	@DisplayName("Flux.zip() con tre sorgenti combina l'ennesimo elemento di ciascuna in una Tuple3")
	void testZipCombinesThreeSourcesIntoTuple3() {
		StepVerifier.create(Flux.zip(Flux.just("body-1"), Flux.just("engine-1"), Flux.just("tires-1")))
					.expectNextMatches(t -> t.getT1().equals("body-1") && t.getT2().equals("engine-1") && t.getT3().equals("tires-1"))
					.expectComplete()
					.verify();
	}
}
