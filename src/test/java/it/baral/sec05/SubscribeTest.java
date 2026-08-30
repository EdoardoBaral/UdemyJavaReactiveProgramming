package it.baral.sec05;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Subscribe Tests")
class SubscribeTest {

	@Test
	@DisplayName("subscribe() senza consumer permette comunque a doOnNext() di osservare gli elementi")
	void testSubscribeWithoutConsumerStillLetsDoOnNextObserveElements() {
		List<Integer> received = new ArrayList<>();
		Flux.range(1, 5)
			.doOnNext(received::add)
			.subscribe();

		assertEquals(List.of(1, 2, 3, 4, 5), received);
	}

	@Test
	@DisplayName("subscribe() senza consumer permette comunque a doOnComplete() di osservare il completamento")
	void testSubscribeWithoutConsumerStillLetsDoOnCompleteObserveCompletion() {
		AtomicInteger completedCount = new AtomicInteger(0);
		Flux.range(1, 5)
			.doOnComplete(completedCount::incrementAndGet)
			.subscribe();

		assertEquals(1, completedCount.get());
	}

	@Test
	@DisplayName("subscribe() senza consumer permette comunque a doOnError() di osservare l'errore")
	void testSubscribeWithoutConsumerStillLetsDoOnErrorObserveError() {
		Throwable[] observedError = {null};
		Flux.error(new RuntimeException("boom"))
			.doOnError(err -> observedError[0] = err)
			.subscribe();

		assertEquals("boom", observedError[0].getMessage());
	}
}
