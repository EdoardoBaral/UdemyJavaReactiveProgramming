package it.baral.sec02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MonoSubscribe Tests")
class MonoSubscribeTest {

	@Test
	@DisplayName("subscribe() con consumer per elemento riceve il valore")
	void testSubscribeWithConsumer() {
		Integer[] received = {null};
		Mono.just(1).subscribe(value -> received[0] = value);

		assertEquals(1, received[0]);
	}

	@Test
	@DisplayName("subscribe() con consumer per elemento e errore")
	void testSubscribeWithConsumerAndErrorConsumer() {
		Integer[] received = {null};
		Throwable[] error = {null};

		Mono.just(1).subscribe(
			value -> received[0] = value,
			err -> error[0] = err
		);

		assertEquals(1, received[0]);
		assertNull(error[0]);
	}

	@Test
	@DisplayName("subscribe() con consumer per elemento, errore e completamento")
	void testSubscribeWithAllConsumers() {
		Integer[] received = {null};
		Throwable[] error = {null};
		boolean[] completed = {false};

		Mono.just(1).subscribe(
			value -> received[0] = value,
			err -> error[0] = err,
			() -> completed[0] = true
		);

		assertEquals(1, received[0]);
		assertNull(error[0]);
		assertTrue(completed[0]);
	}

	@Test
	@DisplayName("subscribe() con subscription handler")
	void testSubscribeWithSubscriptionHandler() {
		Subscription[] subscription = {null};

		Mono.just(1).subscribe(
			value -> {},
			err -> {},
			() -> {},
			sub -> subscription[0] = sub
		);

		assertNotNull(subscription[0]);
	}

	@Test
	@DisplayName("subscribe() con errore e error consumer")
	void testSubscribeWithError() {
		Integer[] received = {null};
		Throwable[] error = {null};

		Mono.just(1)
			.map(i -> i / 0)
			.subscribe(
				value -> received[0] = value,
				err -> error[0] = err
			);

		assertNull(received[0]);
		assertNotNull(error[0]);
		assertTrue(error[0] instanceof ArithmeticException);
	}

	@Test
	@DisplayName("subscribe() con Mono.empty() non riceve valori")
	void testSubscribeWithEmpty() {
		boolean[] completed = {false};

		Mono.empty().subscribe(
			value -> {},
			err -> {},
			() -> completed[0] = true
		);

		assertTrue(completed[0]);
	}

	@Test
	@DisplayName("subscribe() esegue il consumer per ogni sottoscrizione")
	void testSubscribeExecutesConsumerForEachSubscription() {
		int[] callCount = {0};

		Mono.just(1).subscribe(value -> callCount[0]++);
		assertEquals(1, callCount[0]);

		Mono.just(1).subscribe(value -> callCount[0]++);
		assertEquals(2, callCount[0]);
	}

	@Test
	@DisplayName("subscribe() con subscription cancel non esegue i consumer")
	void testSubscribeWithCancellation() {
		Integer[] received = {null};
		Subscription[] subscription = {null};

		Mono.just(1).subscribe(
			value -> received[0] = value,
			err -> {},
			() -> {},
			sub -> {
				subscription[0] = sub;
				sub.cancel();
			}
		);

		assertNotNull(subscription[0]);
	}

	@Test
	@DisplayName("subscribe() con map che solleva eccezione propaga l'errore")
	void testSubscribeWithMapError() {
		Throwable[] error = {null};

		Mono.just("1")
			.map(s -> Integer.parseInt(s) / 0)
			.subscribe(
				value -> {},
				err -> error[0] = err
			);

		assertNotNull(error[0]);
		assertTrue(error[0] instanceof ArithmeticException);
	}

	@Test
	@DisplayName("subscribe() senza consumer completa normalmente")
	void testSubscribeWithoutConsumer() {
		assertDoesNotThrow(() -> Mono.just(1).subscribe());
	}
}
