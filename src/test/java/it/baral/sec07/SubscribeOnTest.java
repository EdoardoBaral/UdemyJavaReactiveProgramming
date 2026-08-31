package it.baral.sec07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("SubscribeOn Tests")
class SubscribeOnTest {

	@Test
	@DisplayName("subscribeOn sposta sulla scheduler indicato sia la generazione della sorgente sia gli operatori a valle")
	void testSubscribeOnMovesEntireChainToSpecifiedScheduler() {
		AtomicReference<String> generatingThreadName = new AtomicReference<>();
		AtomicReference<String> downstreamThreadName = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sink -> {
									  		generatingThreadName.set(Thread.currentThread().getName());
									  		sink.next(1);
									  		sink.complete();
									  	  })
								  .cast(Integer.class)
								  .doOnNext(v -> downstreamThreadName.set(Thread.currentThread().getName()))
								  .subscribeOn(Schedulers.boundedElastic());

		flux.blockLast();

		assertEquals(generatingThreadName.get(), downstreamThreadName.get());
		assertTrue(generatingThreadName.get().startsWith("boundedElastic-"));
		assertNotEquals(Thread.currentThread().getName(), generatingThreadName.get());
	}

	@Test
	@DisplayName("subscribeOn sposta l'esecuzione sullo scheduler indicato indipendentemente dal thread chiamante")
	void testSubscribeOnAppliesIndependentlyOfCallingThread() throws InterruptedException {
		AtomicReference<String> sub1ThreadName = new AtomicReference<>();
		AtomicReference<String> sub2ThreadName = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sink -> {
									  		sink.next(1);
									  		sink.complete();
									  	  })
								  .cast(Integer.class)
								  .doOnNext(v -> {})
								  .subscribeOn(Schedulers.boundedElastic());
		CountDownLatch latch = new CountDownLatch(2);

		Runnable runnable1 = () -> flux.subscribe(v -> sub1ThreadName.set(Thread.currentThread().getName()), e -> {}, latch::countDown);
		Runnable runnable2 = () -> flux.subscribe(v -> sub2ThreadName.set(Thread.currentThread().getName()), e -> {}, latch::countDown);

		Thread thread1 = Thread.ofPlatform().start(runnable1);
		Thread thread2 = Thread.ofPlatform().start(runnable2);

		assertTrue(latch.await(5, TimeUnit.SECONDS), "Entrambe le sottoscrizioni devono completare entro il timeout");
		assertTrue(sub1ThreadName.get().startsWith("boundedElastic-"));
		assertTrue(sub2ThreadName.get().startsWith("boundedElastic-"));
		assertNotEquals(thread1.getName(), sub1ThreadName.get());
		assertNotEquals(thread2.getName(), sub2ThreadName.get());
	}
}
