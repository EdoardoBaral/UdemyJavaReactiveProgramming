package it.baral.sec02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MonoFromCallable Tests")
class MonoFromCallableTest {

	@Test
	@DisplayName("fromCallable() esegue il callable e emette il risultato")
	void testFromCallableExecutesAndEmits() {
		Mono<Integer> mono = Mono.fromCallable(() -> sum(List.of(1, 2, 3)));

		StepVerifier.create(mono)
					.expectNext(6)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("fromCallable() è lazy: il callable non viene eseguito fino alla sottoscrizione")
	void testFromCallableIsLazy() {
		boolean[] executed = {false};
		Mono<Integer> mono = Mono.fromCallable(() -> {
			executed[0] = true;
			return sum(List.of(1, 2, 3, 4, 5));
		});

		assertFalse(executed[0], "Il callable non deve essere eseguito prima della sottoscrizione");

		StepVerifier.create(mono)
					.expectNext(15)
					.expectComplete()
					.verify();

		assertTrue(executed[0], "Il callable deve essere eseguito dopo la sottoscrizione");
	}

	@Test
	@DisplayName("fromCallable() propaga le eccezioni checked come errore")
	void testFromCallablePropagatesCheckedExceptions() {
		Mono<String> mono = Mono.fromCallable(() -> {
			throw new InterruptedException("Interrupted");
		});

		StepVerifier.create(mono)
					.expectError(InterruptedException.class)
					.verify();
	}

	@Test
	@DisplayName("fromCallable() propaga le eccezioni unchecked come errore")
	void testFromCallablePropagatesUncheckedExceptions() {
		Mono<Integer> mono = Mono.fromCallable(() -> 1 / 0);

		StepVerifier.create(mono)
					.expectError(ArithmeticException.class)
					.verify();
	}

	@Test
	@DisplayName("fromCallable() esegue il callable per ogni sottoscrizione")
	void testFromCallableExecutesForEachSubscription() {
		int[] executionCount = {0};
		Mono<Integer> mono = Mono.fromCallable(() -> {
			executionCount[0]++;
			return executionCount[0];
		});

		StepVerifier.create(mono)
					.expectNext(1)
					.expectComplete()
					.verify();

		StepVerifier.create(mono)
					.expectNext(2)
					.expectComplete()
					.verify();
	}

	private static int sum(List<Integer> list) {
		return list.stream()
				   .mapToInt(Integer::intValue)
				   .sum();
	}
}
