package it.baral.sec03;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FluxDefer Tests")
class FluxDeferTest {

	@Test
	@DisplayName("defer() rimanda la creazione del Flux alla sottoscrizione")
	void testDeferDelaysCreation() {
		AtomicInteger creationCount = new AtomicInteger(0);

		Flux<Integer> deferredFlux = Flux.defer(() -> {
										  	creationCount.incrementAndGet();
										  	return Flux.fromIterable(List.of(1, 2, 3, 4, 5));
										  });

		assertEquals(0, creationCount.get(), "Il supplier non deve essere eseguito finché non avviene la sottoscrizione");

		StepVerifier.create(deferredFlux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();

		assertEquals(1, creationCount.get(), "Il supplier deve essere eseguito una volta dopo la sottoscrizione");
	}

	@Test
	@DisplayName("defer() esegue il supplier per ogni sottoscrizione")
	void testDeferExecutesSupplierForEachSubscription() {
		AtomicInteger executionCount = new AtomicInteger(0);

		Flux<Integer> deferredFlux = Flux.defer(() -> {
										  	executionCount.incrementAndGet();
										  	return Flux.fromIterable(List.of(1, 2, 3));
										  });

		StepVerifier.create(deferredFlux)
					.expectNext(1, 2, 3)
					.expectComplete()
					.verify();

		assertEquals(1, executionCount.get());

		StepVerifier.create(deferredFlux)
					.expectNext(1, 2, 3)
					.expectComplete()
					.verify();

		assertEquals(2, executionCount.get(), "Il supplier deve essere eseguito per ogni sottoscrizione");
	}

	@Test
	@DisplayName("Flux non differito ricalcola comunque la sequenza ad ogni sottoscrizione poiché e' cold")
	void testNonDeferredFluxIsAlreadyLazilyEvaluatedPerSubscription() {
		List<Integer> list = List.of(1, 2, 3, 4, 5);
		Flux<Integer> flux = Flux.fromIterable(list);

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();

		StepVerifier.create(flux)
					.expectNext(1, 2, 3, 4, 5)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("defer() propaga errori generati dal supplier")
	void testDeferPropagatesErrorFromSupplier() {
		Flux<Integer> deferredFlux = Flux.defer(() -> {
										  	throw new RuntimeException("Supplier error");
										  });

		StepVerifier.create(deferredFlux)
					.expectError(RuntimeException.class)
					.verify();
	}

	@Test
	@DisplayName("defer() con Flux.empty() non emette valori")
	void testDeferWithEmptyFlux() {
		Flux<String> deferredFlux = Flux.defer(Flux::empty);

		StepVerifier.create(deferredFlux)
					.expectComplete()
					.verify();
	}
}
