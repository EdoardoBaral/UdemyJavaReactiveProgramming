package it.baral.sec02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("MonoDefer Tests")
class MonoDeferTest {

	@Test
	@DisplayName("defer() rimanda la creazione del Mono alla sottoscrizione")
	void testDeferDelaysCreation() {
		AtomicInteger creationCount = new AtomicInteger(0);

		Mono<Integer> deferredMono = Mono.defer(() -> {
			creationCount.incrementAndGet();
			return Mono.just(sum(List.of(1, 2, 3, 4, 5)));
		});

		assertEquals(0, creationCount.get(), "Il supplier non deve essere eseguito finché non avviene la sottoscrizione");

		StepVerifier.create(deferredMono)
					.expectNext(15)
					.expectComplete()
					.verify();

		assertEquals(1, creationCount.get(), "Il supplier deve essere eseguito una volta dopo la sottoscrizione");
	}

	@Test
	@DisplayName("defer() esegue il supplier per ogni sottoscrizione")
	void testDeferExecutesSupplierForEachSubscription() {
		AtomicInteger executionCount = new AtomicInteger(0);

		Mono<Integer> deferredMono = Mono.defer(() -> {
			executionCount.incrementAndGet();
			return Mono.just(sum(List.of(1, 2, 3)));
		});

		StepVerifier.create(deferredMono)
					.expectNext(6)
					.expectComplete()
					.verify();

		assertEquals(1, executionCount.get());

		StepVerifier.create(deferredMono)
					.expectNext(6)
					.expectComplete()
					.verify();

		assertEquals(2, executionCount.get(), "Il supplier deve essere eseguito per ogni sottoscrizione");
	}

	@Test
	@DisplayName("defer() restituisce il Mono creato dal supplier")
	void testDeferReturnsMonoFromSupplier() {
		Mono<String> deferredMono = Mono.defer(() -> Mono.just("deferred value"));

		StepVerifier.create(deferredMono)
					.expectNext("deferred value")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("defer() propaga errori dal supplier")
	void testDeferPropagatesErrorFromSupplier() {
		Mono<Integer> deferredMono = Mono.defer(() -> {
			throw new RuntimeException("Supplier error");
		});

		StepVerifier.create(deferredMono)
					.expectError(RuntimeException.class)
					.verify();
	}

	@Test
	@DisplayName("defer() con Mono.empty() non emette valori")
	void testDeferWithEmptyMono() {
		Mono<String> deferredMono = Mono.defer(Mono::empty);

		StepVerifier.create(deferredMono)
					.expectComplete()
					.verify();
	}

	private static int sum(List<Integer> list) {
		return list.stream()
				   .mapToInt(Integer::intValue)
				   .sum();
	}
}
