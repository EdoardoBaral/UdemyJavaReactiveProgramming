package it.baral.sec02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("DemoDefaultSubscriber Tests")
class DemoDefaultSubscriberTest {

	@Test
	@DisplayName("Mono.just() consente sottoscrizioni multiple")
	void testMultipleSubscriptions() {
		Mono<Integer> mono = Mono.just(1);

		StepVerifier.create(mono)
					.expectNext(1)
					.expectComplete()
					.verify();

		StepVerifier.create(mono)
					.expectNext(1)
					.expectComplete()
					.verify();

		StepVerifier.create(mono)
					.expectNext(1)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Ogni sottoscrizione riceve il medesimo valore")
	void testEachSubscriptionReceivesSameValue() {
		Integer expectedValue = 1;
		Mono<Integer> mono = Mono.just(expectedValue);

		StepVerifier.create(mono)
					.expectNext(expectedValue)
					.expectComplete()
					.verify();

		StepVerifier.create(mono)
					.expectNext(expectedValue)
					.expectComplete()
					.verify();
	}
}
