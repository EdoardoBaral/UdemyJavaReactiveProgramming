package it.baral.sec03;

import it.baral.sec03.helper.NameGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("FluxVSList Tests")
class FluxVSListTest {

	@Test
	@DisplayName("L'approccio con List restituisce tutti i nomi generati in un'unica collezione")
	void testListApproachReturnsAllGeneratedNames() {
		List<String> names = NameGenerator.generateNamesList(2);

		assertEquals(2, names.size());
		names.forEach(name -> {
			assertNotNull(name);
			assertFalse(name.isBlank());
		});
	}

	@Test
	@DisplayName("L'approccio con Flux emette lo stesso numero di nomi dell'approccio con List")
	void testFluxApproachEmitsSameCountAsListApproach() {
		Flux<String> namesFlux = NameGenerator.generateNamesFlux(2);

		StepVerifier.create(namesFlux)
					.expectNextCount(2)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("L'approccio con Flux non genera alcun nome prima della sottoscrizione")
	void testFluxApproachGeneratesNothingBeforeSubscription() {
		AtomicInteger emittedCount = new AtomicInteger(0);

		Flux<String> namesFlux = NameGenerator.generateNamesFlux(2)
											   .doOnNext(name -> emittedCount.incrementAndGet());

		assertEquals(0, emittedCount.get(), "Nessun nome deve essere generato prima della sottoscrizione");

		StepVerifier.create(namesFlux)
					.expectNextCount(2)
					.expectComplete()
					.verify();

		assertEquals(2, emittedCount.get());
	}
}
