package it.baral.sec08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("FluxCreate Tests")
class FluxCreateTest {

	@Test
	@DisplayName("Senza sink.onRequest(), il ciclo di generazione ignora la domanda a valle ed emette comunque tutti i valori")
	void testGenerationIgnoresDownstreamDemandAndEmitsAllValues() {
		AtomicInteger generatedCount = new AtomicInteger();
		Flux<Integer> flux = Flux.create(sink -> {
									  		for(int i=1; i<=10 && !sink.isCancelled(); i++) {
									  			generatedCount.incrementAndGet();
									  			sink.next(i);
									  		}
									  		sink.complete();
									  	  })
								  .cast(Integer.class);

		StepVerifier.create(flux, 0)
					.expectSubscription()
					.thenRequest(10)
					.expectNext(1)
					.expectNextCount(9)
					.expectComplete()
					.verify();

		assertEquals(10, generatedCount.get());
	}

	@Test
	@DisplayName("La cancellazione della sottoscrizione, se rilevata durante l'esecuzione asincrona, interrompe la generazione prima del completamento")
	void testGenerationStopsEarlyWhenCancelledWhileRunningAsynchronously() throws InterruptedException {
		AtomicInteger generatedCount = new AtomicInteger();
		Flux<Integer> flux = Flux.create(sink -> {
									  		for(int i=1; i<=1000 && !sink.isCancelled(); i++) {
									  			generatedCount.incrementAndGet();
									  			try {
									  				Thread.sleep(5);
									  			} catch (InterruptedException e) {
									  				Thread.currentThread().interrupt();
									  			}
									  			sink.next(i);
									  		}
									  		sink.complete();
									  	  })
								  .cast(Integer.class)
								  .subscribeOn(Schedulers.parallel());

		Disposable subscription = flux.subscribe();
		Thread.sleep(50);
		subscription.dispose();
		Thread.sleep(50);
		int countRightAfterCancel = generatedCount.get();
		Thread.sleep(100);

		assertTrue(countRightAfterCancel < 1000, "La generazione deve fermarsi prima di completare tutti i 1000 elementi");
		assertEquals(countRightAfterCancel, generatedCount.get(), "Dopo la cancellazione non devono essere generati altri elementi");
	}
}
