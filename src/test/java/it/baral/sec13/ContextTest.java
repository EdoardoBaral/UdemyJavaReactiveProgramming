package it.baral.sec13;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("Context Tests")
class ContextTest {

	@Test
	@DisplayName("deferContextual() legge un valore scritto a valle tramite contextWrite()")
	void testDeferContextualReadsValueWrittenByContextWrite() {
		Mono<String> mono = welcomeMono().contextWrite(reactor.util.context.Context.of("user", "Edoardo"));

		StepVerifier.create(mono)
					.expectNext("welcome Edoardo")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("deferContextual() fallisce quando la chiave attesa non e' presente nel Context")
	void testDeferContextualFailsWhenExpectedKeyIsMissing() {
		Mono<String> mono = welcomeMono().contextWrite(reactor.util.context.Context.of("a", "b"));

		StepVerifier.create(mono)
					.expectErrorMessage("unauthorized")
					.verify();
	}

	private Mono<String> welcomeMono() {
		return Mono.deferContextual(ctx -> {
			if(ctx.hasKey("user")) {
				return Mono.just("welcome %s".formatted(ctx.get("user").toString()));
			} else {
				return Mono.error(new RuntimeException("unauthorized"));
			}
		});
	}
}
