package it.baral.sec11;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ExternalServiceDemo Tests")
class ExternalServiceDemoTest {

	@Test
	@DisplayName("retryOnServerError() ritenta automaticamente in presenza di un ServerError, fino al successo")
	void testRetryOnServerErrorRetriesOnlyServerErrors() {
		AtomicInteger counter = new AtomicInteger();
		Mono<String> mono = Mono.fromSupplier(() -> {
									 if(counter.incrementAndGet() < 3) {
										 throw new ServerError();
									 }
									 return "value";
								 });

		StepVerifier.withVirtualTime(() -> mono.retryWhen(ExternalServiceDemo.retryOnServerError()))
					.expectSubscription()
					.thenAwait(Duration.ofSeconds(2))
					.expectNext("value")
					.expectComplete()
					.verify();

		assertEquals(3, counter.get());
	}

	@Test
	@DisplayName("retryOnServerError() non ritenta in presenza di un ClientError, propagandolo subito")
	void testRetryOnServerErrorDoesNotRetryClientErrors() {
		AtomicInteger counter = new AtomicInteger();
		Mono<String> mono = Mono.fromSupplier(() -> {
									 counter.incrementAndGet();
									 throw new ClientError();
								 });

		StepVerifier.create(mono.retryWhen(ExternalServiceDemo.retryOnServerError()))
					.expectError(ClientError.class)
					.verify();

		assertEquals(1, counter.get());
	}
}
