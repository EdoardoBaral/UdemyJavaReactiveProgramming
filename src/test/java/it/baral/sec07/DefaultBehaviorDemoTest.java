package it.baral.sec07;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DefaultBehaviorDemo Tests")
class DefaultBehaviorDemoTest {

	@Test
	@DisplayName("Senza subscribeOn/publishOn, la generazione dei valori avviene sul thread che chiama subscribe()")
	void testGenerationRunsOnSubscribingThread() {
		AtomicReference<String> generatingThreadName = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sink -> {
									  		generatingThreadName.set(Thread.currentThread().getName());
									  		sink.next(1);
									  		sink.complete();
									  	  })
								  .cast(Integer.class);

		StepVerifier.create(flux)
					.expectNext(1)
					.expectComplete()
					.verify();

		assertEquals(Thread.currentThread().getName(), generatingThreadName.get());
	}

	@Test
	@DisplayName("Ogni nuova sottoscrizione a un Flux cold innesca una nuova esecuzione della sorgente")
	void testEachSubscriptionTriggersNewSourceExecution() {
		AtomicInteger invocationCount = new AtomicInteger();
		Flux<Integer> flux = Flux.create(sink -> {
									  		invocationCount.incrementAndGet();
									  		sink.next(1);
									  		sink.complete();
									  	  })
								  .cast(Integer.class);

		flux.subscribe();
		flux.subscribe();

		assertEquals(2, invocationCount.get());
	}

	@Test
	@DisplayName("Due sottoscrittori su thread separati eseguono la generazione ciascuno sul proprio thread")
	void testTwoSubscribersOnSeparateThreadsExecuteGenerationOnTheirOwnThread() throws InterruptedException {
		AtomicReference<String> sub1ThreadName = new AtomicReference<>();
		AtomicReference<String> sub2ThreadName = new AtomicReference<>();
		Flux<Integer> flux = Flux.create(sink -> {
									  		sink.next(1);
									  		sink.complete();
									  	  })
								  .cast(Integer.class);
		CountDownLatch latch = new CountDownLatch(2);

		Runnable runnable1 = () -> flux.subscribe(v -> sub1ThreadName.set(Thread.currentThread().getName()), e -> {}, latch::countDown);
		Runnable runnable2 = () -> flux.subscribe(v -> sub2ThreadName.set(Thread.currentThread().getName()), e -> {}, latch::countDown);

		Thread thread1 = Thread.ofPlatform().start(runnable1);
		Thread thread2 = Thread.ofPlatform().start(runnable2);

		assertTrue(latch.await(5, TimeUnit.SECONDS), "Entrambe le sottoscrizioni devono completare entro il timeout");
		assertEquals(thread1.getName(), sub1ThreadName.get());
		assertEquals(thread2.getName(), sub2ThreadName.get());
		assertNotEquals(sub1ThreadName.get(), sub2ThreadName.get());
	}
}
