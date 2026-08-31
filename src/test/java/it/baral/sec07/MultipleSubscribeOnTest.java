package it.baral.sec07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MultipleSubscribeOn Tests")
class MultipleSubscribeOnTest {

	@Test
	@DisplayName("Tra due subscribeOn annidati, vince quello piu' vicino alla sorgente")
	void testFirstSubscribeOnDeterminesSchedulerOverSecond() {
		Scheduler firstScheduler = Schedulers.newParallel("first-scheduler", 1);
		Scheduler secondScheduler = Schedulers.newParallel("second-scheduler", 1);
		try {
			AtomicReference<String> generatingThreadName = new AtomicReference<>();
			Flux<Integer> flux = Flux.create(sink -> {
										  		sink.next(1);
										  		sink.complete();
										  	  })
									  .cast(Integer.class)
									  .subscribeOn(firstScheduler)
									  .doOnNext(v -> generatingThreadName.set(Thread.currentThread().getName()))
									  .subscribeOn(secondScheduler);

			flux.blockLast();

			assertTrue(generatingThreadName.get().startsWith("first-scheduler-"));
		} finally {
			firstScheduler.dispose();
			secondScheduler.dispose();
		}
	}

	@Test
	@DisplayName("subscribeOn(Schedulers.immediate()) si comporta come se subscribeOn non fosse presente")
	void testSubscribeOnImmediateActsAsNoOp() {
		AtomicReference<String> generatingThreadName = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sink -> {
									  		generatingThreadName.set(Thread.currentThread().getName());
									  		sink.next(1);
									  		sink.complete();
									  	  })
								  .cast(Integer.class)
								  .subscribeOn(Schedulers.immediate());

		StepVerifier.create(flux)
					.expectNext(1)
					.expectComplete()
					.verify();

		assertEquals(Thread.currentThread().getName(), generatingThreadName.get());
	}
}
