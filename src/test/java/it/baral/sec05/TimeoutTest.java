package it.baral.sec05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@DisplayName("Timeout Tests")
class TimeoutTest {

	@Test
	@DisplayName("timeout() non scatta quando il Mono risponde prima della scadenza")
	void testTimeoutDoesNotTriggerWhenMonoRespondsBeforeDeadline() {
		StepVerifier.withVirtualTime(() -> Mono.just("product")
											   .delayElement(Duration.ofSeconds(3))
											   .timeout(Duration.ofSeconds(10)))
					.expectSubscription()
					.thenAwait(Duration.ofSeconds(3))
					.expectNext("product")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("timeout() scatta e propaga un TimeoutException quando il Mono risponde dopo la scadenza")
	void testTimeoutTriggersAndPropagatesTimeoutExceptionWhenMonoRespondsAfterDeadline() {
		StepVerifier.withVirtualTime(() -> Mono.just("product")
											   .delayElement(Duration.ofSeconds(3))
											   .timeout(Duration.ofSeconds(1)))
					.expectSubscription()
					.thenAwait(Duration.ofSeconds(1))
					.expectError(TimeoutException.class)
					.verify();
	}

	@Test
	@DisplayName("timeout() con fallback emette il valore del Mono di fallback quando scatta")
	void testTimeoutWithFallbackEmitsFallbackMonoValueWhenItTriggers() {
		Mono<String> primary = Mono.just("primary")
								   .delayElement(Duration.ofSeconds(3));
		Mono<String> fallback = Mono.just("fallback")
									.delayElement(Duration.ofSeconds(1));

		StepVerifier.withVirtualTime(() -> primary.timeout(Duration.ofSeconds(1), fallback))
					.expectSubscription()
					.thenAwait(Duration.ofSeconds(2))
					.expectNext("fallback")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Timeout multipli in cascata: quello piu' stretto applicato a valle scatta per primo")
	void testMultipleCascadedTimeoutsTheStricterDownstreamOneTriggersFirst() {
		Mono<String> primary = Mono.just("primary")
								   .delayElement(Duration.ofSeconds(3));
		Mono<String> fallback = Mono.just("fallback")
									.delayElement(Duration.ofSeconds(2));
		Mono<String> mono = primary.timeout(Duration.ofSeconds(1), fallback);

		StepVerifier.withVirtualTime(() -> mono.timeout(Duration.ofMillis(200)))
					.expectSubscription()
					.thenAwait(Duration.ofMillis(200))
					.expectError(TimeoutException.class)
					.verify();
	}
}
