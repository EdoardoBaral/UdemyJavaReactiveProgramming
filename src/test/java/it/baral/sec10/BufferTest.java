package it.baral.sec10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Buffer Tests")
class BufferTest {

	@Test
	@DisplayName("buffer() senza argomenti raccoglie tutti gli elementi in un'unica lista al completamento")
	void testBufferWithoutArgumentsCollectsAllElementsIntoSingleListAtCompletion() {
		StepVerifier.create(Flux.range(1, 5).buffer())
					.expectNext(List.of(1, 2, 3, 4, 5))
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("buffer(n) raggruppa gli elementi in liste della dimensione fissa indicata")
	void testBufferWithFixedSizeGroupsElementsIntoListsOfGivenSize() {
		StepVerifier.create(Flux.range(1, 7).buffer(3))
					.expectNext(List.of(1, 2, 3))
					.expectNext(List.of(4, 5, 6))
					.expectNext(List.of(7))
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("buffer(Duration) raggruppa in piu' liste gli elementi emessi entro finestre temporali successive, senza perderne alcuno")
	void testBufferWithDurationGroupsElementsEmittedWithinTimeWindows() {
		AtomicInteger totalReceived = new AtomicInteger();
		AtomicInteger bufferCount = new AtomicInteger();

		StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofMillis(100))
												.take(5)
												.buffer(Duration.ofMillis(220))
												.doOnNext(list -> {
													totalReceived.addAndGet(list.size());
													bufferCount.incrementAndGet();
												}))
					.thenAwait(Duration.ofSeconds(2))
					.thenConsumeWhile(list -> true)
					.verifyComplete();

		assertEquals(5, totalReceived.get());
		assertTrue(bufferCount.get() > 1, "Gli elementi devono essere raggruppati in piu' di un buffer temporale");
	}

	@Test
	@DisplayName("bufferTimeout(n, Duration) emette non appena si raggiunge la dimensione indicata oppure trascorre il timeout, a seconda di cosa avviene prima")
	void testBufferTimeoutEmitsWhenSizeReachedOrTimeoutElapsesWhicheverFirst() {
		StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofMillis(100))
												.take(7)
												.concatWith(Flux.never())
												.bufferTimeout(3, Duration.ofSeconds(1)))
					.thenAwait(Duration.ofSeconds(1))
					.expectNext(List.of(0L, 1L, 2L))
					.thenAwait(Duration.ofSeconds(1))
					.expectNext(List.of(3L, 4L, 5L))
					.thenAwait(Duration.ofSeconds(1))
					.expectNext(List.of(6L))
					.thenCancel()
					.verify();
	}
}
