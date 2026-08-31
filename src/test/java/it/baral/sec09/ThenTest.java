package it.baral.sec09;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Then Tests")
class ThenTest {

	@Test
	@DisplayName("then(Mono) ignora gli elementi emessi dalla sorgente e passa l'esecuzione al Mono indicato dopo il completamento")
	void testThenIgnoresUpstreamElementsAndSwitchesToGivenMonoAfterCompletion() {
		AtomicBoolean secondMonoRan = new AtomicBoolean();
		Mono<String> result = Flux.just("a", "b", "c")
								  .then(Mono.fromSupplier(() -> {
									  		secondMonoRan.set(true);
									  		return "done";
								  		 }));

		StepVerifier.create(result)
					.expectNext("done")
					.expectComplete()
					.verify();

		assertTrue(secondMonoRan.get());
	}

	@Test
	@DisplayName("then(Mono<Void>) completa senza emettere alcun valore, dopo aver eseguito il Mono indicato")
	void testThenWithVoidMonoCompletesWithoutEmittingAnyValue() {
		AtomicBoolean notified = new AtomicBoolean();
		Mono<Void> result = Flux.just("a", "b", "c")
								 .then(Mono.fromRunnable(() -> notified.set(true)));

		StepVerifier.create(result)
					.expectComplete()
					.verify();

		assertTrue(notified.get());
	}

	@Test
	@DisplayName("then(Mono) non sottoscrive il secondo Mono se la sorgente termina con un errore")
	void testThenDoesNotSwitchToSecondMonoIfUpstreamErrors() {
		AtomicBoolean secondMonoSubscribed = new AtomicBoolean();
		Mono<String> result = Flux.just("a", "b")
								  .concatWith(Flux.error(new RuntimeException("boom")))
								  .then(Mono.just("done").doOnSubscribe(s -> secondMonoSubscribed.set(true)));

		StepVerifier.create(result)
					.expectError(RuntimeException.class)
					.verify();

		assertFalse(secondMonoSubscribed.get());
	}
}
