package it.baral.sec07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("VirtualThreads Tests")
class VirtualThreadsTest {

	@Test
	@DisplayName("Uno scheduler basato su un executor a virtual thread esegue la sottoscrizione su un virtual thread")
	void testSchedulerBackedByVirtualThreadsExecutesOnVirtualThread() {
		Scheduler virtualThreadScheduler = Schedulers.fromExecutorService(Executors.newVirtualThreadPerTaskExecutor());
		try {
			AtomicBoolean isVirtual = new AtomicBoolean();
			Flux<Integer> flux = Flux.just(1)
									  .subscribeOn(virtualThreadScheduler)
									  .doOnNext(v -> isVirtual.set(Thread.currentThread().isVirtual()));

			flux.blockLast();

			assertTrue(isVirtual.get());
		} finally {
			virtualThreadScheduler.dispose();
		}
	}

	@Test
	@DisplayName("Lo scheduler boundedElastic di default non esegue la sottoscrizione su un virtual thread")
	void testDefaultBoundedElasticSchedulerDoesNotExecuteOnVirtualThread() {
		AtomicBoolean isVirtual = new AtomicBoolean();
		Flux<Integer> flux = Flux.just(1)
								  .subscribeOn(Schedulers.boundedElastic())
								  .doOnNext(v -> isVirtual.set(Thread.currentThread().isVirtual()));

		flux.blockLast();

		assertFalse(isVirtual.get());
	}
}
