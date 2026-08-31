package it.baral.sec07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("PublishOn Tests")
class PublishOnTest {

	@Test
	@DisplayName("publishOn non altera il thread su cui viene eseguita la sorgente, a differenza di subscribeOn")
	void testPublishOnDoesNotAffectSourceGenerationThread() {
		AtomicReference<String> generatingThreadName = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sink -> {
									  		generatingThreadName.set(Thread.currentThread().getName());
									  		sink.next(1);
									  		sink.complete();
									  	  })
								  .cast(Integer.class)
								  .publishOn(Schedulers.boundedElastic());

		flux.blockLast();

		assertEquals(Thread.currentThread().getName(), generatingThreadName.get());
	}

	@Test
	@DisplayName("publishOn sposta l'esecuzione degli operatori a valle sullo scheduler indicato")
	void testPublishOnMovesDownstreamOperatorsToSpecifiedScheduler() {
		AtomicReference<String> downstreamThreadName = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sink -> {
									  		sink.next(1);
									  		sink.complete();
									  	  })
								  .cast(Integer.class)
								  .publishOn(Schedulers.boundedElastic())
								  .doOnNext(v -> downstreamThreadName.set(Thread.currentThread().getName()));

		flux.blockLast();

		assertTrue(downstreamThreadName.get().startsWith("boundedElastic-"));
	}

	@Test
	@DisplayName("Due publishOn in sequenza spostano ciascuno gli operatori successivi sul proprio scheduler")
	void testSuccessivePublishOnMoveOperatorsToDifferentSchedulers() {
		AtomicReference<String> afterFirstPublishOnThreadName = new AtomicReference<>();
		AtomicReference<String> afterSecondPublishOnThreadName = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sink -> {
									  		sink.next(1);
									  		sink.complete();
									  	  })
								  .cast(Integer.class)
								  .publishOn(Schedulers.parallel())
								  .doOnNext(v -> afterFirstPublishOnThreadName.set(Thread.currentThread().getName()))
								  .publishOn(Schedulers.boundedElastic())
								  .doOnNext(v -> afterSecondPublishOnThreadName.set(Thread.currentThread().getName()));

		flux.blockLast();

		assertTrue(afterFirstPublishOnThreadName.get().startsWith("parallel-"));
		assertTrue(afterSecondPublishOnThreadName.get().startsWith("boundedElastic-"));
		assertNotEquals(afterFirstPublishOnThreadName.get(), afterSecondPublishOnThreadName.get());
	}
}
