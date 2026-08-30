package it.baral.sec02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MonoFromSupplier Tests")
class MonoFromSupplierTest {

	@Test
	@DisplayName("fromSupplier() esegue il supplier e emette il risultato")
	void testFromSupplierExecutesAndEmits() {
		Mono<Integer> mono = Mono.fromSupplier(() -> sum(List.of(1, 2, 3)));

		StepVerifier.create(mono)
					.expectNext(6)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("fromSupplier() è lazy: il supplier non viene eseguito fino alla sottoscrizione")
	void testFromSupplierIsLazy() {
		boolean[] executed = {false};
		Mono<Integer> mono = Mono.fromSupplier(() -> {
			executed[0] = true;
			return sum(List.of(1, 2, 3, 4, 5));
		});

		assertFalse(executed[0], "Il supplier non deve essere eseguito prima della sottoscrizione");

		StepVerifier.create(mono)
					.expectNext(15)
					.expectComplete()
					.verify();

		assertTrue(executed[0], "Il supplier deve essere eseguito dopo la sottoscrizione");
	}

	@Test
	@DisplayName("fromSupplier() propaga le eccezioni unchecked come errore")
	void testFromSupplierPropagatesExceptions() {
		Mono<Integer> mono = Mono.fromSupplier(() -> 1 / 0);

		StepVerifier.create(mono)
					.expectError(ArithmeticException.class)
					.verify();
	}

	@Test
	@DisplayName("fromSupplier() esegue il supplier per ogni sottoscrizione")
	void testFromSupplierExecutesForEachSubscription() {
		int[] executionCount = {0};
		Mono<Integer> mono = Mono.fromSupplier(() -> {
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

	@Test
	@DisplayName("fromSupplier() con valore zero funziona correttamente")
	void testFromSupplierWithZeroValue() {
		Mono<Integer> mono = Mono.fromSupplier(() -> sum(List.of()));

		StepVerifier.create(mono)
					.expectNext(0)
					.expectComplete()
					.verify();
	}

	private static int sum(List<Integer> list) {
		return list.stream()
				   .mapToInt(Integer::intValue)
				   .sum();
	}
}
