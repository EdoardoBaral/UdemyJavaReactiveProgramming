package it.baral.sec05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ErrorHandling Tests")
class ErrorHandlingTest {

	@Test
	@DisplayName("onErrorReturn() sostituisce qualsiasi errore con il valore fisso fornito")
	void testOnErrorReturnReplacesAnyErrorWithGivenFixedValue() {
		Flux<Integer> flux = Flux.range(1, 5)
								 .map(i -> i == 3 ? 1 / 0 : i)
								 .onErrorReturn(-1);

		StepVerifier.create(flux)
					.expectNext(1, 2, -1)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("onErrorReturn() filtrato per tipo sostituisce solo l'eccezione corrispondente")
	void testOnErrorReturnFilteredByTypeReplacesOnlyMatchingException() {
		Flux<Integer> flux = Flux.range(1, 5)
								 .map(i -> i == 3 ? 1 / 0 : i)
								 .onErrorReturn(ArithmeticException.class, -1);

		StepVerifier.create(flux)
					.expectNext(1, 2, -1)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("onErrorReturn() filtrato per un tipo non corrispondente propaga l'errore originale")
	void testOnErrorReturnFilteredByNonMatchingTypePropagatesOriginalError() {
		Flux<Integer> flux = Flux.range(1, 5)
								 .map(i -> i == 3 ? 1 / 0 : i)
								 .onErrorReturn(IllegalStateException.class, -1);

		StepVerifier.create(flux)
					.expectNext(1, 2)
					.expectError(ArithmeticException.class)
					.verify();
	}

	@Test
	@DisplayName("onErrorResume() sostituisce qualsiasi errore con il Flux di fallback fornito")
	void testOnErrorResumeReplacesAnyErrorWithGivenFallbackFlux() {
		Flux<Integer> flux = Flux.range(1, 5)
								 .map(i -> i == 3 ? 1 / 0 : i)
								 .onErrorResume(ex -> Flux.just(100, 200));

		StepVerifier.create(flux)
					.expectNext(1, 2, 100, 200)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("onErrorResume() filtrato per un tipo non corrispondente propaga l'errore originale")
	void testOnErrorResumeFilteredByNonMatchingTypePropagatesOriginalError() {
		Flux<Integer> flux = Flux.range(1, 5)
								 .map(i -> i == 3 ? 1 / 0 : i)
								 .onErrorResume(IllegalStateException.class, ex -> Flux.just(100));

		StepVerifier.create(flux)
					.expectNext(1, 2)
					.expectError(ArithmeticException.class)
					.verify();
	}

	@Test
	@DisplayName("onErrorComplete() trasforma l'errore in un completamento silenzioso")
	void testOnErrorCompleteTurnsErrorIntoSilentCompletion() {
		Flux<Integer> flux = Flux.range(1, 5)
								 .map(i -> i == 3 ? 1 / 0 : i)
								 .onErrorComplete();

		StepVerifier.create(flux)
					.expectNext(1, 2)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("onErrorContinue() ignora l'elemento che ha causato l'errore e prosegue con i successivi")
	void testOnErrorContinueSkipsFailingElementAndContinuesWithSubsequentOnes() {
		List<Integer> failedItems = new ArrayList<>();
		Flux<Integer> flux = Flux.range(1, 5)
								 .map(i -> i == 3 ? 1 / 0 : i)
								 .onErrorContinue((ex, item) -> failedItems.add((Integer) item));

		StepVerifier.create(flux)
					.expectNext(1, 2, 4, 5)
					.expectComplete()
					.verify();

		assertEquals(List.of(3), failedItems);
	}
}
