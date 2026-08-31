package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Merge Tests")
class MergeTest {

	@Test
	@DisplayName("Flux.merge() sottoscrive tutte le sorgenti contemporaneamente, a differenza della concatenazione")
	void testMergeSubscribesToAllSourcesConcurrently() {
		List<String> events = Collections.synchronizedList(new ArrayList<>());
		Flux<Integer> flux = Flux.merge(mergeParticipant(1, events), mergeParticipant(2, events));

		StepVerifier.create(flux)
					.expectNextCount(2)
					.expectComplete()
					.verify();

		int active = 0;
		int max = 0;
		for(String event : events) {
			active += event.startsWith("subscribe-") ? 1 : -1;
			max = Math.max(max, active);
		}
		assertTrue(max > 1, "Le sorgenti devono risultare sottoscritte contemporaneamente");
	}

	@Test
	@DisplayName("mergeWith() emette i valori nell'ordine di arrivo, non nell'ordine di dichiarazione delle sorgenti")
	void testMergeWithEmitsValuesInArrivalOrderNotDeclarationOrder() {
		Flux<Integer> flux = Flux.just(1)
								  .delayElements(Duration.ofMillis(50))
								  .mergeWith(Flux.just(2).delayElements(Duration.ofMillis(10)));

		StepVerifier.create(flux)
					.expectNext(2, 1)
					.expectComplete()
					.verify();
	}

	private Flux<Integer> mergeParticipant(int value, List<String> events) {
		return Flux.just(value)
				   .delayElements(Duration.ofMillis(20))
				   .doOnSubscribe(s -> events.add("subscribe-" +value))
				   .doOnComplete(() -> events.add("complete-" +value));
	}
}
