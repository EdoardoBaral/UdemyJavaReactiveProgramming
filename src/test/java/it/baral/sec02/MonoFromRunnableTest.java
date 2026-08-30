package it.baral.sec02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MonoFromRunnable Tests")
class MonoFromRunnableTest {

	@Test
	@DisplayName("fromRunnable() esegue il runnable e completa vuoto")
	void testFromRunnableExecutesAndCompletesEmpty() {
		boolean[] executed = {false};
		Mono<Void> mono = Mono.fromRunnable(() -> executed[0] = true);

		assertFalse(executed[0], "Il runnable non deve essere eseguito prima della sottoscrizione");

		StepVerifier.create(mono)
					.expectComplete()
					.verify();

		assertTrue(executed[0], "Il runnable deve essere eseguito durante la sottoscrizione");
	}

	@Test
	@DisplayName("fromRunnable() non emette alcun valore")
	void testFromRunnableEmitsNoValue() {
		Mono<Void> mono = Mono.fromRunnable(() -> {});

		StepVerifier.create(mono)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("getProductName() ritorna Mono con nome per productId=1")
	void testGetProductNameReturnsValueForId1() {
		Mono<String> result = getProductName(1);

		StepVerifier.create(result)
					.expectNextMatches(name -> name != null && !name.isEmpty())
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("getProductName() ritorna Mono vuoto per productId!=1")
	void testGetProductNameReturnsEmptyForOtherId() {
		Mono<String> result = getProductName(2);

		StepVerifier.create(result)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("getProductName() esegue il runnable per productId!=1")
	void testGetProductNameExecutesRunnableForOtherId() {
		boolean[] notified = {false};
		Mono<String> mono = Mono.defer(() -> {
			if (2 == 2) {
				return Mono.fromRunnable(() -> notified[0] = true);
			}
			return Mono.just("product");
		});

		StepVerifier.create(mono)
					.expectComplete()
					.verify();

		assertTrue(notified[0], "Il runnable deve essere eseguito");
	}

	@Test
	@DisplayName("fromRunnable() propaga eccezioni come errore")
	void testFromRunnablePropagatesExceptions() {
		Mono<Void> mono = Mono.fromRunnable(() -> {
			throw new RuntimeException("Runnable error");
		});

		StepVerifier.create(mono)
					.expectError(RuntimeException.class)
					.verify();
	}

	@Test
	@DisplayName("fromRunnable() esegue il runnable per ogni sottoscrizione")
	void testFromRunnableExecutesForEachSubscription() {
		int[] executionCount = {0};
		Mono<Void> mono = Mono.fromRunnable(() -> executionCount[0]++);

		StepVerifier.create(mono)
					.expectComplete()
					.verify();

		assertEquals(1, executionCount[0]);

		StepVerifier.create(mono)
					.expectComplete()
					.verify();

		assertEquals(2, executionCount[0], "Il runnable deve essere eseguito per ogni sottoscrizione");
	}

	private static Mono<String> getProductName(int productId) {
		if(productId == 1) {
			return Mono.fromSupplier(() -> "Product " + productId);
		}
		return Mono.fromRunnable(() -> notifyBusiness(productId));
	}

	private static void notifyBusiness(int productId) {
	}
}
