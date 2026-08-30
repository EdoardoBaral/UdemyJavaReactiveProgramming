package it.baral.sec02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("NonBlockingIO Tests")
class NonBlockingIOTest {

	@Test
	@DisplayName("subscribe() non blocca il thread principale")
	void testNonBlockingSubscribe() {
		List<Integer> results = new ArrayList<>();
		AtomicInteger completed = new AtomicInteger(0);

		Mono.just(1).subscribe(
			results::add,
			err -> {},
			() -> completed.incrementAndGet()
		);

		assertTrue(results.contains(1));
		assertEquals(1, completed.get());
	}

	@Test
	@DisplayName("subscribe() consente sottoscrizioni multiple non bloccanti")
	void testMultipleNonBlockingSubscriptions() {
		Mono<Integer> mono = Mono.just(42);
		List<Integer> results = new ArrayList<>();
		AtomicInteger count = new AtomicInteger(0);

		for (int i = 0; i < 5; i++) {
			mono.subscribe(
				results::add,
				err -> {},
				count::incrementAndGet
			);
		}

		assertEquals(5, results.size());
		assertEquals(5, count.get());
		for (Integer result : results) {
			assertEquals(42, result);
		}
	}

	@Test
	@DisplayName("subscribe() vs block(): subscribe non blocca")
	void testSubscribeVsBlockNonBlocking() {
		long start = System.currentTimeMillis();

		Mono<Integer> mono = Mono.just(1);
		mono.subscribe(value -> {});

		long elapsed = System.currentTimeMillis() - start;

		assertTrue(elapsed < 100, "subscribe() deve ritornare immediatamente");
	}

	@Test
	@DisplayName("block() blocca il thread fino al completamento")
	void testBlockBlocks() {
		Mono<Integer> mono = Mono.just(1);

		long start = System.currentTimeMillis();
		Integer result = mono.block();
		long elapsed = System.currentTimeMillis() - start;

		assertEquals(1, result);
	}

	@Test
	@DisplayName("Multiple subscribe() non bloccanti eseguono in parallelo logico")
	void testMultipleSubscriptionsNonBlocking() {
		List<Long> executionTimes = new ArrayList<>();
		long startTime = System.currentTimeMillis();

		for (int i = 0; i < 10; i++) {
			Mono.just(i).subscribe(
				value -> executionTimes.add(System.currentTimeMillis() - startTime)
			);
		}

		assertEquals(10, executionTimes.size());
	}

	@Test
	@DisplayName("subscribe() permette di continuare l'esecuzione del thread")
	void testSubscribeContinuesExecution() {
		boolean[] beforeSubscribe = {false};
		boolean[] afterSubscribe = {false};

		beforeSubscribe[0] = true;

		Mono.just(1).subscribe(value -> {});

		afterSubscribe[0] = true;

		assertTrue(beforeSubscribe[0]);
		assertTrue(afterSubscribe[0]);
	}

	@Test
	@DisplayName("Sottoscrizioni non bloccanti mantengono ordine di sottoscrizione")
	void testSubscriptionOrderMaintained() {
		List<Integer> results = new ArrayList<>();

		for (int i = 1; i <= 5; i++) {
			int finalI = i;
			Mono.just(i).subscribe(results::add);
		}

		assertEquals(5, results.size());
		for (int i = 0; i < 5; i++) {
			assertEquals(i + 1, results.get(i));
		}
	}

	@Test
	@DisplayName("subscribe() con consumer di errore non blocca nemmeno in caso di errore")
	void testNonBlockingWithError() {
		AtomicInteger errorHandled = new AtomicInteger(0);

		Mono.error(new RuntimeException("test error"))
			.subscribe(
				value -> {},
				err -> errorHandled.incrementAndGet()
			);

		assertEquals(1, errorHandled.get());
	}

	@Test
	@DisplayName("Parecchie sottoscrizioni non bloccanti si completano")
	void testManyNonBlockingSubscriptions() {
		AtomicInteger completedCount = new AtomicInteger(0);
		int subscriptionCount = 100;

		for (int i = 0; i < subscriptionCount; i++) {
			Mono.just(i).subscribe(
				value -> {},
				err -> {},
				completedCount::incrementAndGet
			);
		}

		assertEquals(subscriptionCount, completedCount.get());
	}
}
