package it.baral.sec12;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("SinkOne Tests")
class SinkOneTest {

	@Test
	@DisplayName("tryEmitValue() consegna il valore al subscriber")
	void testTryEmitValueDeliversValueToSubscriber() {
		Sinks.One<Object> sink = Sinks.one();

		StepVerifier.create(sink.asMono())
					.then(() -> sink.tryEmitValue("Hello"))
					.expectNext("Hello")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("tryEmitEmpty() completa il Mono senza emettere alcun valore")
	void testTryEmitEmptyCompletesWithoutValue() {
		Sinks.One<Object> sink = Sinks.one();

		StepVerifier.create(sink.asMono())
					.then(sink::tryEmitEmpty)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("tryEmitError() termina il Mono con l'errore indicato")
	void testTryEmitErrorTerminatesWithError() {
		Sinks.One<Object> sink = Sinks.one();

		StepVerifier.create(sink.asMono())
					.then(() -> sink.tryEmitError(new RuntimeException("ooops")))
					.expectError(RuntimeException.class)
					.verify();
	}

	@Test
	@DisplayName("Tutti i subscriber sottoscritti prima dell'emissione ricevono lo stesso valore")
	void testMultipleSubscribersAllReceiveTheSameEmittedValue() {
		Sinks.One<Object> sink = Sinks.one();
		Mono<Object> mono = sink.asMono();
		List<Object> received1 = new ArrayList<>();
		List<Object> received2 = new ArrayList<>();
		mono.subscribe(received1::add);
		mono.subscribe(received2::add);

		sink.tryEmitValue("Hello");

		assertEquals(List.of("Hello"), received1);
		assertEquals(List.of("Hello"), received2);
	}

	@Test
	@DisplayName("emitValue() invoca l'EmitFailureHandler quando un secondo tentativo di emissione fallisce perche' il sink e' gia' terminato")
	void testEmitValueInvokesFailureHandlerOnSecondAttempt() {
		Sinks.One<Object> sink = Sinks.one();
		sink.asMono().subscribe();

		List<Sinks.EmitResult> handlerResults = new ArrayList<>();
		sink.emitValue("hi", (signalType, emitResult) -> {
			handlerResults.add(emitResult);
			return false;
		});
		sink.emitValue("hello", (signalType, emitResult) -> {
			handlerResults.add(emitResult);
			return false;
		});

		assertEquals(List.of(Sinks.EmitResult.FAIL_TERMINATED), handlerResults);
	}
}
