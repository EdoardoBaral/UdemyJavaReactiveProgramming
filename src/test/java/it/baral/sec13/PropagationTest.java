package it.baral.sec13;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Propagation Tests")
class PropagationTest {

	@Test
	@DisplayName("Il Context scritto a valle e' visibile a tutti i producer coinvolti, indipendentemente dallo scheduler su cui vengono eseguiti")
	void testContextIsPropagatedToAllProducersRegardlessOfTheirScheduler() {
		AtomicReference<Boolean> producer1HasUserKey = new AtomicReference<>();
		AtomicReference<Boolean> producer2HasUserKey = new AtomicReference<>();

		Mono<String> producer1 = Mono.<String>deferContextual(ctx -> {
										  producer1HasUserKey.set(ctx.hasKey("user"));
										  return Mono.empty();
									  })
									  .subscribeOn(Schedulers.boundedElastic());

		Mono<String> producer2 = Mono.<String>deferContextual(ctx -> {
										  producer2HasUserKey.set(ctx.hasKey("user"));
										  return Mono.empty();
									  })
									  .subscribeOn(Schedulers.parallel());

		Flux<String> flux = welcomeMono().concatWith(Flux.merge(producer1, producer2))
										  .contextWrite(reactor.util.context.Context.of("user", "Edoardo"));

		StepVerifier.create(flux)
					.expectNext("welcome Edoardo")
					.expectComplete()
					.verify();

		assertEquals(true, producer1HasUserKey.get());
		assertEquals(true, producer2HasUserKey.get());
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
