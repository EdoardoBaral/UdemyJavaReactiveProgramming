package it.baral.sec05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DoCallbacks Tests")
class DoCallbacksTest {

	@Test
	@DisplayName("doOnNext() viene invocato per ogni elemento emesso, nell'ordine di emissione")
	void testDoOnNextIsInvokedForEachEmittedElementInOrder() {
		List<Integer> observed = new ArrayList<>();
		Flux<Integer> flux = Flux.range(1, 4)
								 .doOnNext(observed::add);

		StepVerifier.create(flux)
					.expectNextCount(4)
					.expectComplete()
					.verify();

		assertEquals(List.of(1, 2, 3, 4), observed);
	}

	@Test
	@DisplayName("doOnComplete() viene invocato esattamente una volta al completamento normale")
	void testDoOnCompleteIsInvokedExactlyOnceOnNormalCompletion() {
		AtomicInteger completedCount = new AtomicInteger(0);
		Flux<Integer> flux = Flux.range(1, 4)
								 .doOnComplete(completedCount::incrementAndGet);

		StepVerifier.create(flux)
					.expectNextCount(4)
					.expectComplete()
					.verify();

		assertEquals(1, completedCount.get());
	}

	@Test
	@DisplayName("doOnError() riceve l'eccezione che ha terminato il Flux")
	void testDoOnErrorReceivesTheExceptionThatTerminatedTheFlux() {
		Throwable[] observedError = {null};
		RuntimeException expectedError = new RuntimeException("boom");
		Flux<Integer> flux = Flux.<Integer>error(expectedError)
								 .doOnError(err -> observedError[0] = err);

		StepVerifier.create(flux)
					.expectError(RuntimeException.class)
					.verify();

		assertEquals(expectedError, observedError[0]);
	}

	@Test
	@DisplayName("doOnCancel() viene invocato quando la sottoscrizione viene cancellata")
	void testDoOnCancelIsInvokedWhenSubscriptionIsCancelled() {
		AtomicInteger cancelCount = new AtomicInteger(0);
		Flux<Integer> flux = Flux.range(1, 10)
								 .doOnCancel(cancelCount::incrementAndGet);

		StepVerifier.create(flux)
					.thenCancel()
					.verify();

		assertEquals(1, cancelCount.get());
	}

	@Test
	@DisplayName("doFinally() viene invocato con ON_COMPLETE al completamento normale")
	void testDoFinallyIsInvokedWithOnCompleteOnNormalCompletion() {
		SignalType[] observedSignal = {null};
		Flux<Integer> flux = Flux.range(1, 3)
								 .doFinally(signal -> observedSignal[0] = signal);

		StepVerifier.create(flux)
					.expectNextCount(3)
					.expectComplete()
					.verify();

		assertEquals(SignalType.ON_COMPLETE, observedSignal[0]);
	}

	@Test
	@DisplayName("doFinally() viene invocato con ON_ERROR in caso di errore")
	void testDoFinallyIsInvokedWithOnErrorOnError() {
		SignalType[] observedSignal = {null};
		Flux<Integer> flux = Flux.<Integer>error(new RuntimeException("boom"))
								 .doFinally(signal -> observedSignal[0] = signal);

		StepVerifier.create(flux)
					.expectError(RuntimeException.class)
					.verify();

		assertEquals(SignalType.ON_ERROR, observedSignal[0]);
	}

	@Test
	@DisplayName("doFinally() viene invocato con CANCEL in caso di cancellazione")
	void testDoFinallyIsInvokedWithCancelOnCancellation() {
		SignalType[] observedSignal = {null};
		Flux<Integer> flux = Flux.range(1, 10)
								 .doFinally(signal -> observedSignal[0] = signal);

		StepVerifier.create(flux)
					.thenCancel()
					.verify();

		assertEquals(SignalType.CANCEL, observedSignal[0]);
	}

	@Test
	@DisplayName("doFirst() viene invocato prima che avvenga la sottoscrizione")
	void testDoFirstIsInvokedBeforeSubscriptionHappens() {
		AtomicInteger doFirstCount = new AtomicInteger(0);
		Flux<Integer> flux = Flux.range(1, 3)
								 .doFirst(doFirstCount::incrementAndGet);

		assertEquals(0, doFirstCount.get(), "doFirst non deve essere invocato prima della sottoscrizione");

		StepVerifier.create(flux)
					.expectNextCount(3)
					.expectComplete()
					.verify();

		assertEquals(1, doFirstCount.get());
	}
}
