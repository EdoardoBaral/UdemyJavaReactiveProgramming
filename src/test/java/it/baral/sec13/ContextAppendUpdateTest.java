package it.baral.sec13;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("ContextAppendUpdate Tests")
class ContextAppendUpdateTest {

	@Test
	@DisplayName("Piu' contextWrite() successivi vengono uniti in un unico Context visibile a monte")
	void testMultipleContextWriteCallsAreMergedIntoASingleContext() {
		Mono<String> mono = welcomeMono().contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
										  .contextWrite(reactor.util.context.Context.of("a", "b").put("c", "d").put("e", "f"));

		StepVerifier.create(mono)
					.expectNext("welcome Edoardo")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Una scrittura con Context.empty() piu' a valle non cancella le chiavi gia' scritte da contextWrite() piu' a monte")
	void testWritingAnEmptyContextDownstreamDoesNotEraseKeysWrittenFurtherUpstream() {
		Mono<String> mono = welcomeMono().contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
										  .contextWrite(reactor.util.context.Context.of("a", "b"))
										  .contextWrite(ctx -> reactor.util.context.Context.empty());

		StepVerifier.create(mono)
					.expectNext("welcome Edoardo")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Un contextWrite() piu' a monte (piu' vicino alla sorgente) sovrascrive una chiave gia' scritta da un contextWrite() piu' a valle")
	void testContextWriteFurtherUpstreamOverwritesKeyWrittenDownstream() {
		Mono<String> mono = welcomeMono().contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
										  .contextWrite(reactor.util.context.Context.of("user", "Paolo"));

		StepVerifier.create(mono)
					.expectNext("welcome Edoardo")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("Un contextWrite() piu' a monte puo' rimuovere una chiave gia' scritta piu' a valle tramite ctx.delete()")
	void testContextWriteFurtherUpstreamCanDeleteKeyWrittenDownstream() {
		Mono<Boolean> mono = Mono.deferContextual(ctx -> Mono.just(ctx.hasKey("c")))
								  .contextWrite(ctx -> ctx.delete("c"))
								  .contextWrite(reactor.util.context.Context.of("c", "d"));

		StepVerifier.create(mono)
					.expectNext(false)
					.expectComplete()
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
