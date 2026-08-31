package it.baral.sec07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EventLoopIssueFix Tests")
class EventLoopIssueFixTest {

	@Test
	@DisplayName("Senza publishOn, l'elaborazione a valle viene eseguita sullo stesso thread della sorgente (simula il blocco dell'event loop)")
	void testWithoutPublishOnDownstreamProcessingRunsOnSourceThread() {
		Scheduler eventLoop = Schedulers.newSingle("netty-event-loop");
		try {
			AtomicReference<String> processingThreadName = new AtomicReference<>();
			Mono<String> mono = Mono.fromSupplier(() -> "value")
									 .subscribeOn(eventLoop)
									 .map(v -> {
									 	processingThreadName.set(Thread.currentThread().getName());
									 	return v +"-processed";
									 });

			mono.block();

			assertTrue(processingThreadName.get().startsWith("netty-event-loop-"));
		} finally {
			eventLoop.dispose();
		}
	}

	@Test
	@DisplayName("Con publishOn(boundedElastic), l'elaborazione bloccante viene spostata fuori dal thread della sorgente")
	void testPublishOnMovesBlockingProcessingOffSourceThread() {
		Scheduler eventLoop = Schedulers.newSingle("netty-event-loop");
		try {
			AtomicReference<String> processingThreadName = new AtomicReference<>();
			Mono<String> mono = Mono.fromSupplier(() -> "value")
									 .subscribeOn(eventLoop)
									 .publishOn(Schedulers.boundedElastic())
									 .map(v -> {
									 	processingThreadName.set(Thread.currentThread().getName());
									 	return v +"-processed";
									 });

			mono.block();

			assertTrue(processingThreadName.get().startsWith("boundedElastic-"));
			assertFalse(processingThreadName.get().startsWith("netty-event-loop-"));
		} finally {
			eventLoop.dispose();
		}
	}
}
