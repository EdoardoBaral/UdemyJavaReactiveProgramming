package it.baral.sec12;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

@DisplayName("SinkUnicast Tests")
class SinkUnicastTest {

	@Test
	@DisplayName("Un sink unicast bufferizza gli elementi emessi prima di un subscriber e li consegna tutti alla sottoscrizione")
	void testUnicastSinkBuffersValuesEmittedBeforeAnySubscriberAndDeliversOnSubscription() {
		Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
		Flux<String> flux = sink.asFlux();

		sink.tryEmitNext("a");
		sink.tryEmitNext("b");
		sink.tryEmitNext("c");

		StepVerifier.create(flux)
					.expectNext("a", "b", "c")
					.thenCancel()
					.verify();
	}

	@Test
	@DisplayName("Un secondo tentativo di sottoscrizione a un sink unicast riceve un errore")
	void testSecondSubscriberToUnicastSinkReceivesAnError() {
		Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
		Flux<String> flux = sink.asFlux();

		flux.subscribe();

		StepVerifier.create(flux)
					.expectError(IllegalStateException.class)
					.verify();
	}
}
