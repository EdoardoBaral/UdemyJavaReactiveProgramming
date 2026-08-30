package it.baral.sec04;

import it.baral.sec04.helper.NameGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("FluxCreateRefactor Tests")
class FluxCreateRefactorTest {

	@Test
	@DisplayName("Il Flux creato da NameGenerator emette un nome per ogni chiamata a generate()")
	void testFluxEmitsOneNamePerGenerateCall() {
		NameGenerator nameGenerator = new NameGenerator();
		Flux<String> flux = Flux.create(nameGenerator);
		List<String> received = new ArrayList<>();
		flux.subscribe(received::add);

		for (int i = 0; i < 10; i++) {
			nameGenerator.generate();
		}

		assertEquals(10, received.size());
		received.forEach(name -> assertFalse(name.isBlank()));
	}

	@Test
	@DisplayName("Il Flux creato da NameGenerator non emette nulla prima della prima generate()")
	void testFluxEmitsNothingBeforeFirstGenerateCall() {
		NameGenerator nameGenerator = new NameGenerator();
		Flux<String> flux = Flux.create(nameGenerator);
		List<String> received = new ArrayList<>();
		flux.subscribe(received::add);

		assertTrue(received.isEmpty(), "Nessun nome deve essere emesso prima della chiamata a generate()");
	}

	@Test
	@DisplayName("Il Flux creato da NameGenerator resta aperto: nessun onComplete viene emesso")
	void testFluxCreatedFromNameGeneratorNeverCompletesOnItsOwn() {
		NameGenerator nameGenerator = new NameGenerator();
		Flux<String> flux = Flux.create(nameGenerator);

		StepVerifier.create(flux.take(3))
					.then(() -> {
						nameGenerator.generate();
						nameGenerator.generate();
						nameGenerator.generate();
					})
					.expectNextCount(3)
					.expectComplete()
					.verify();
	}
}
