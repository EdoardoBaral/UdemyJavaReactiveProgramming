package it.baral.sec07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SubscribeOnPublishOn Tests")
class SubscribeOnPublishOnTest {

	@Test
	@DisplayName("subscribeOn sposta la generazione della sorgente sul proprio scheduler indipendentemente dalla posizione nella catena")
	void testSubscribeOnMovesSourceGenerationRegardlessOfPosition() {
		AtomicReference<String> generatingThreadName = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sink -> {
									  		generatingThreadName.set(Thread.currentThread().getName());
									  		sink.next(1);
									  		sink.complete();
									  	  })
								  .cast(Integer.class)
								  .publishOn(Schedulers.parallel())
								  .subscribeOn(Schedulers.boundedElastic());

		flux.blockLast();

		assertTrue(generatingThreadName.get().startsWith("boundedElastic-"));
	}

	@Test
	@DisplayName("publishOn sposta sul proprio scheduler gli operatori successivi, distinto da quello usato da subscribeOn per la sorgente")
	void testPublishOnMovesDownstreamOperatorsToDifferentSchedulerThanSubscribeOn() {
		AtomicReference<String> generatingThreadName = new AtomicReference<>();
		AtomicReference<String> afterPublishOnThreadName = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sink -> {
									  		generatingThreadName.set(Thread.currentThread().getName());
									  		sink.next(1);
									  		sink.complete();
									  	  })
								  .cast(Integer.class)
								  .publishOn(Schedulers.parallel())
								  .doOnNext(v -> afterPublishOnThreadName.set(Thread.currentThread().getName()))
								  .subscribeOn(Schedulers.boundedElastic());

		flux.blockLast();

		assertTrue(afterPublishOnThreadName.get().startsWith("parallel-"));
		assertNotEquals(generatingThreadName.get(), afterPublishOnThreadName.get());
	}
}
