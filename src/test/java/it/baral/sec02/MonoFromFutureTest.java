package it.baral.sec02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("MonoFromFuture Tests")
class MonoFromFutureTest {

	@Test
	@DisplayName("fromFuture() emette il risultato di una CompletableFuture completata")
	void testFromFutureWithCompletedFuture() {
		CompletableFuture<String> future = CompletableFuture.completedFuture("completed");
		Mono<String> mono = Mono.fromFuture(future);

		StepVerifier.create(mono)
					.expectNext("completed")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("fromFuture() emette il risultato quando la CompletableFuture si completa")
	void testFromFutureWithAsyncFuture() {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "async result");
		Mono<String> mono = Mono.fromFuture(future);

		StepVerifier.create(mono)
					.expectNext("async result")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("fromFuture() propaga gli errori della CompletableFuture")
	void testFromFuturePropagatesErrors() {
		CompletableFuture<String> future = new CompletableFuture<>();
		future.completeExceptionally(new RuntimeException("Future error"));

		Mono<String> mono = Mono.fromFuture(future);

		StepVerifier.create(mono)
					.expectError(RuntimeException.class)
					.verify();
	}

	@Test
	@DisplayName("fromFuture() attende il completamento della Future")
	void testFromFutureWaitsForCompletion() throws InterruptedException {
		CompletableFuture<String> future = new CompletableFuture<>();
		Mono<String> mono = Mono.fromFuture(future);

		new Thread(() -> {
			try {
				Thread.sleep(100);
				future.complete("delayed result");
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();

		StepVerifier.create(mono)
					.expectNext("delayed result")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("fromFuture() con valore intero")
	void testFromFutureWithIntegerValue() {
		CompletableFuture<Integer> future = CompletableFuture.completedFuture(42);
		Mono<Integer> mono = Mono.fromFuture(future);

		StepVerifier.create(mono)
					.expectNext(42)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("fromFuture() non riesegue la sottoscrizione per la stessa Future")
	void testFromFutureDoesNotReexecute() {
		int[] callCount = {0};
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			callCount[0]++;
			return "result";
		});

		Mono<String> mono = Mono.fromFuture(future);

		StepVerifier.create(mono)
					.expectNext("result")
					.expectComplete()
					.verify();

		int firstCallCount = callCount[0];

		StepVerifier.create(mono)
					.expectNext("result")
					.expectComplete()
					.verify();

		assertEquals(firstCallCount, callCount[0], "La Future non deve essere rieseguita");
	}
}
