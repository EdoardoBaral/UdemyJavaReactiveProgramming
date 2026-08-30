package it.baral.sec02;

import it.baral.sec01.subscriber.SubscriberImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test JUnit per la classe {@link MonoJust}. Verifica il comportamento di
 * {@link Mono#just(Object)} in diversi scenari: emissione del valore,
 * completamento e risposta alle richieste successive e cancellazione.
 */
@DisplayName("MonoJust Tests")
class MonoJustTest {

	private SubscriberImpl subscriber;

	@BeforeEach
	void setUp() {
		subscriber = new SubscriberImpl();
	}

	@Test
	@DisplayName("Mono.just() emette il valore fornito")
	void testMonoJustEmitsValue() {
		String expectedValue = "Edoardo";
		Mono<String> mono = Mono.just(expectedValue);

		mono.subscribe(subscriber);

		assertNotNull(subscriber.getSubscription(), "La sottoscrizione non deve essere nulla");
	}

	@Test
	@DisplayName("Mono.just() completa dopo l'emissione")
	void testMonoJustCompletes() {
		Mono<String> mono = Mono.just("Edoardo");

		StepVerifier.create(mono)
					.expectNext("Edoardo")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Mono.just() ignora richieste multiple")
	void testMonoJustIgnoresMultipleRequests() {
		String value = "Edoardo";
		Mono<String> mono = Mono.just(value);

		mono.subscribe(subscriber);
		subscriber.getSubscription().request(3);
		subscriber.getSubscription().request(3);

		StepVerifier.create(Mono.just(value))
					.expectNext(value)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Mono.just() ignora la cancellazione dopo l'emissione")
	void testMonoJustIgnoresCancellation() {
		Mono<String> mono = Mono.just("Edoardo");

		mono.subscribe(subscriber);
		subscriber.getSubscription().cancel();

		StepVerifier.create(Mono.just("Edoardo"))
					.expectNext("Edoardo")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Mono.just() con null genera NullPointerException")
	void testMonoJustWithNull() {
		assertThrows(NullPointerException.class, () -> Mono.just(null));
	}

	@Test
	@DisplayName("Mono.just() emette esattamente un valore")
	void testMonoJustEmitsExactlyOneValue() {
		String value = "Edoardo";

		StepVerifier.create(Mono.just(value))
					.expectNextCount(1)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Scenario completo: sottoscrizione, richieste e cancellazione")
	void testCompleteScenario() {
		String expectedValue = "Edoardo";
		Mono<String> mono = Mono.just(expectedValue);

		mono.subscribe(subscriber);
		assertNotNull(subscriber.getSubscription());

		subscriber.getSubscription().request(3);
		subscriber.getSubscription().request(3);
		subscriber.getSubscription().cancel();

		StepVerifier.create(Mono.just(expectedValue))
					.expectNext(expectedValue)
					.expectComplete()
					.verify();
	}
}