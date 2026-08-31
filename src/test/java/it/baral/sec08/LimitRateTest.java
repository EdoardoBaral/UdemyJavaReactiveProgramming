package it.baral.sec08;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("LimitRate Tests")
class LimitRateTest {

	@Test
	@DisplayName("limitRate() suddivide la richiesta illimitata a valle in piu' lotti verso la sorgente")
	void testLimitRateRequestsUpstreamInBatchesInsteadOfUnbounded() {
		List<Long> requestedAmounts = new CopyOnWriteArrayList<>();
		Flux<Integer> flux = Flux.range(1, 50)
								  .doOnRequest(requestedAmounts::add)
								  .limitRate(5);

		StepVerifier.create(flux)
					.expectNext(1)
					.expectNextCount(49)
					.expectComplete()
					.verify();

		assertTrue(requestedAmounts.size() > 1, "limitRate deve suddividere la richiesta in piu' lotti anziche' un'unica richiesta illimitata");
		assertEquals(5L, requestedAmounts.get(0));
	}

	@Test
	@DisplayName("Senza limitRate(), un subscriber con domanda illimitata invia alla sorgente un'unica richiesta illimitata")
	void testWithoutLimitRateUpstreamReceivesSingleUnboundedRequest() {
		List<Long> requestedAmounts = new CopyOnWriteArrayList<>();
		Flux<Integer> flux = Flux.range(1, 50)
								  .doOnRequest(requestedAmounts::add);

		StepVerifier.create(flux)
					.expectNext(1)
					.expectNextCount(49)
					.expectComplete()
					.verify();

		assertEquals(1, requestedAmounts.size());
		assertEquals(Long.MAX_VALUE, requestedAmounts.get(0));
	}
}
