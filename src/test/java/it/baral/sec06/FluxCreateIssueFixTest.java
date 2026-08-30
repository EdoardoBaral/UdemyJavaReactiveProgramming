package it.baral.sec06;

import it.baral.sec04.helper.NameGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FluxCreateIssueFix Tests")
class FluxCreateIssueFixTest {

	@Test
	@DisplayName("share() rende hot il Flux creato da NameGenerator: tutti i sottoscrittori già iscritti ricevono gli stessi valori generati manualmente")
	void testShareMakesFluxHotAndAllSubscribersReceiveSameGeneratedValues() {
		NameGenerator nameGenerator = new NameGenerator();
		Flux<String> flux = Flux.create(nameGenerator).share();

		List<String> received1 = new ArrayList<>();
		List<String> received2 = new ArrayList<>();
		flux.subscribe(received1::add);
		flux.subscribe(received2::add);

		for(int i=0; i<10; i++) {
			nameGenerator.generate();
		}

		assertEquals(10, received1.size());
		assertEquals(received1, received2);
	}

	@Test
	@DisplayName("share() innesca una sola sottoscrizione a monte: un unico valore generato raggiunge comunque entrambi i sottoscrittori condivisi")
	void testShareTriggersOnlyOneUpstreamSubscriptionSharedByAllDownstreamSubscribers() {
		NameGenerator nameGenerator = new NameGenerator();
		Flux<String> flux = Flux.create(nameGenerator).share();

		List<String> received1 = new ArrayList<>();
		List<String> received2 = new ArrayList<>();
		flux.subscribe(received1::add);
		flux.subscribe(received2::add);

		nameGenerator.generate();

		assertEquals(1, received1.size());
		assertEquals(1, received2.size());
		assertEquals(received1, received2);
	}

	@Test
	@DisplayName("Un sottoscrittore che si iscrive dopo una generate() perde il valore già emesso, perché share() non effettua replay")
	void testLateSubscriberMissesAlreadyEmittedValues() {
		NameGenerator nameGenerator = new NameGenerator();
		Flux<String> flux = Flux.create(nameGenerator).share();

		List<String> receivedEarly = new ArrayList<>();
		flux.subscribe(receivedEarly::add);

		nameGenerator.generate();

		List<String> receivedLate = new ArrayList<>();
		flux.subscribe(receivedLate::add);

		nameGenerator.generate();

		assertEquals(2, receivedEarly.size());
		assertEquals(1, receivedLate.size());
	}
}
