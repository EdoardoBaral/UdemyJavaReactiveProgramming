package it.baral.sec08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicReference;

@DisplayName("BackPressureStrategies Tests")
class BackPressureStrategiesTest {

	@Test
	@DisplayName("onBackpressureBuffer() bufferizza tutti gli elementi in eccesso e li consegna appena richiesti, senza perdite")
	void testOnBackpressureBufferDeliversAllValuesEvenWhenProducerOutpacesDemand() {
		AtomicReference<FluxSink<Integer>> sinkRef = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sinkRef::set)
							     .onBackpressureBuffer();

		StepVerifier.create(flux, 0)
					.expectSubscription()
					.then(() -> {
						for(int i=1; i<=10; i++) {
							sinkRef.get().next(i);
						}
					})
					.thenRequest(3)
					.expectNext(1, 2, 3)
					.thenRequest(7)
					.expectNext(4)
					.expectNextCount(6)
					.thenCancel()
					.verify();
	}

	@Test
	@DisplayName("onBackpressureError() termina il flusso con un errore non appena arriva un elemento non richiesto")
	void testOnBackpressureErrorTerminatesWhenProducerExceedsRequestedDemand() {
		AtomicReference<FluxSink<Integer>> sinkRef = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sinkRef::set)
							     .onBackpressureError();

		StepVerifier.create(flux, 0)
					.expectSubscription()
					.then(() -> sinkRef.get().next(1))
					.expectError(IllegalStateException.class)
					.verify();
	}

	@Test
	@DisplayName("onBackpressureBuffer(n) termina il flusso con un errore di overflow (dopo aver consegnato il contenuto gia' bufferizzato) quando il buffer di dimensione fissa viene superato")
	void testOnBackpressureBufferWithFixedSizeTerminatesWhenCapacityExceeded() {
		AtomicReference<FluxSink<Integer>> sinkRef = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sinkRef::set)
							     .onBackpressureBuffer(8);

		StepVerifier.create(flux, 0)
					.expectSubscription()
					.then(() -> {
						for(int i=1; i<=100; i++) {
							sinkRef.get().next(i);
						}
					})
					.thenRequest(Long.MAX_VALUE)
					.expectNextCount(8)
					.expectError(IllegalStateException.class)
					.verify();
	}

	@Test
	@DisplayName("onBackpressureDrop() scarta silenziosamente gli elementi emessi quando non c'e' domanda a valle")
	void testOnBackpressureDropDiscardsValuesThatExceedRequestedDemand() {
		AtomicReference<FluxSink<Integer>> sinkRef = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sinkRef::set)
							     .onBackpressureDrop();

		StepVerifier.create(flux, 0)
					.expectSubscription()
					.then(() -> {
						for(int i=1; i<=5; i++) {
							sinkRef.get().next(i);
						}
					})
					.thenRequest(1)
					.then(() -> sinkRef.get().next(6))
					.expectNext(6)
					.thenCancel()
					.verify();
	}

	@Test
	@DisplayName("onBackpressureLatest() mantiene solo l'ultimo elemento emesso quando non c'e' domanda a valle")
	void testOnBackpressureLatestKeepsOnlyMostRecentValueWhenDemandIsExhausted() {
		AtomicReference<FluxSink<Integer>> sinkRef = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sinkRef::set)
							     .onBackpressureLatest();

		StepVerifier.create(flux, 0)
					.expectSubscription()
					.then(() -> {
						for(int i=1; i<=5; i++) {
							sinkRef.get().next(i);
						}
					})
					.thenRequest(1)
					.expectNext(5)
					.thenCancel()
					.verify();
	}

	@Test
	@DisplayName("Flux.create(consumer, OverflowStrategy.BUFFER) bufferizza gli elementi in eccesso allo stesso modo di onBackpressureBuffer()")
	void testCreateTimeOverflowStrategyBufferDeliversAllValues() {
		AtomicReference<FluxSink<Integer>> sinkRef = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sinkRef::set, FluxSink.OverflowStrategy.BUFFER);

		StepVerifier.create(flux, 0)
					.expectSubscription()
					.then(() -> {
						for(int i=1; i<=5; i++) {
							sinkRef.get().next(i);
						}
					})
					.thenRequest(5)
					.expectNext(1)
					.expectNextCount(4)
					.thenCancel()
					.verify();
	}
}
