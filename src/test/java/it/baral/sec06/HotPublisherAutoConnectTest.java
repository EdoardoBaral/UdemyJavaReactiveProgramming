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

@DisplayName("HotPublisherAutoConnect Tests")
class HotPublisherAutoConnectTest {

	@Test
	@DisplayName("autoConnect(0) avvia l'emissione subito, prima di qualunque sottoscrizione: un sottoscrittore che arriva dopo un ritardo perde gli elementi già emessi nel frattempo")
	void testAutoConnectZeroEmitsBeforeSubscriptionAndLateSubscriberMissesEarlierElements() {
		AtomicReference<Flux<String>> movieFluxRef = new AtomicReference<>();
		List<String> received = new ArrayList<>();

		StepVerifier.withVirtualTime(() -> {
						movieFluxRef.set(movieStream().publish().autoConnect(0));
						return Flux.<String>never();
					})
					.expectSubscription()
					.thenAwait(Duration.ofMillis(3500))
					.then(() -> movieFluxRef.get().subscribe(received::add))
					.thenAwait(Duration.ofSeconds(7))
					.thenCancel()
					.verify();

		assertEquals(List.of("movie scene4", "movie scene5", "movie scene6", "movie scene7",
							  "movie scene8", "movie scene9", "movie scene10"), received);
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
