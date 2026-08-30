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

@DisplayName("HotPublisher Tests")
class HotPublisherTest {

	@Test
	@DisplayName("movieStream() emette 10 scene numerate con cadenza di un secondo l'una")
	void testMovieStreamEmitsTenNumberedScenesOneSecondApart() {
		StepVerifier.withVirtualTime(this::movieStream)
					.expectSubscription()
					.expectNoEvent(Duration.ofSeconds(1))
					.expectNext("movie scene1")
					.expectNoEvent(Duration.ofSeconds(1))
					.expectNext("movie scene2")
					.thenAwait(Duration.ofSeconds(8))
					.expectNextCount(8)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("share(): il secondo sottoscrittore, unendosi dopo, riceve solo gli elementi emessi da quel momento in poi")
	void testSecondSubscriberJoiningLaterReceivesOnlyElementsEmittedFromThatMomentOn() {
		AtomicReference<Flux<String>> movieFluxRef = new AtomicReference<>();
		List<String> received2 = new ArrayList<>();

		StepVerifier.withVirtualTime(() -> {
						Flux<String> movieFlux = movieStream().share();
						movieFluxRef.set(movieFlux);
						return movieFlux;
					})
					.expectSubscription()
					.expectNoEvent(Duration.ofSeconds(1))
					.expectNext("movie scene1")
					.expectNoEvent(Duration.ofSeconds(1))
					.expectNext("movie scene2")
					.then(() -> movieFluxRef.get().take(3).subscribe(received2::add))
					.expectNoEvent(Duration.ofSeconds(1))
					.expectNext("movie scene3")
					.thenAwait(Duration.ofSeconds(7))
					.expectNextCount(7)
					.expectComplete()
					.verify();

		assertEquals(List.of("movie scene3", "movie scene4", "movie scene5"), received2);
	}

	private Flux<String> movieStream() {
		return Flux.generate(() -> 1,
				   (state, sink) -> {
					   sink.next("movie scene" + state);
					   return ++state;
				   })
				   .take(10)
				   .delayElements(Duration.ofSeconds(1))
				   .cast(String.class);
	}
}
