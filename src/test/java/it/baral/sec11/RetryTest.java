package it.baral.sec11;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Retry Tests")
class RetryTest {

	@Test
	@DisplayName("Senza alcun meccanismo di retry, il primo errore emesso termina il flusso")
	void testWithoutRetryPropagatesFirstError() {
		AtomicInteger counter = new AtomicInteger();
		Mono<String> mono = Mono.fromSupplier(() -> {
									 counter.incrementAndGet();
									 throw new RuntimeException("oops");
								 });

		StepVerifier.create(mono)
					.expectError(RuntimeException.class)
					.verify();

		assertEquals(1, counter.get());
	}

	@Test
	@DisplayName("retry(n) ri-sottoscrive la sorgente fino al successo, entro il numero massimo di tentativi")
	void testRetryWithCountResubscribesUntilSuccess() {
		AtomicInteger counter = new AtomicInteger();
		Mono<String> mono = Mono.fromSupplier(() -> {
									 if(counter.incrementAndGet() < 3) {
										 throw new RuntimeException("oops");
									 }
									 return "value";
								 });

		StepVerifier.create(mono.retry(5))
					.expectNext("value")
					.expectComplete()
					.verify();

		assertEquals(3, counter.get());
	}

	@Test
	@DisplayName("retry(n) esaurito il numero massimo di tentativi propaga l'ultimo errore")
	void testRetryWithCountExhaustedPropagatesLastError() {
		AtomicInteger counter = new AtomicInteger();
		Mono<String> mono = Mono.fromSupplier(() -> {
									 counter.incrementAndGet();
									 throw new RuntimeException("oops");
								 });

		StepVerifier.create(mono.retry(2))
					.expectError(RuntimeException.class)
					.verify();

		assertEquals(3, counter.get());
	}

	@Test
	@DisplayName("retryWhen() con filter() non ritenta un errore che non soddisfa la condizione, propagandolo subito")
	void testRetryWhenWithFilterDoesNotRetryNonMatchingException() {
		AtomicInteger counter = new AtomicInteger();
		Mono<String> mono = Mono.fromSupplier(() -> {
									 counter.incrementAndGet();
									 throw new IllegalStateException("not retried");
								 });

		StepVerifier.create(mono.retryWhen(reactor.util.retry.Retry.max(3).filter(err -> err instanceof IllegalArgumentException)))
					.expectError(IllegalStateException.class)
					.verify();

		assertEquals(1, counter.get());
	}

	@Test
	@DisplayName("retryWhen() con filter() ritenta un errore che soddisfa la condizione, fino al successo")
	void testRetryWhenWithFilterRetriesMatchingExceptionUntilSuccess() {
		AtomicInteger counter = new AtomicInteger();
		Mono<String> mono = Mono.fromSupplier(() -> {
									 if(counter.incrementAndGet() < 3) {
										 throw new IllegalStateException("transient");
									 }
									 return "value";
								 });

		StepVerifier.create(mono.retryWhen(reactor.util.retry.Retry.max(5).filter(err -> err instanceof IllegalStateException)))
					.expectNext("value")
					.expectComplete()
					.verify();

		assertEquals(3, counter.get());
	}

	@Test
	@DisplayName("retryWhen() con fixedDelay() attende il ritardo indicato prima di ogni nuovo tentativo")
	void testRetryWhenWithFixedDelayWaitsBetweenAttempts() {
		AtomicInteger counter = new AtomicInteger();
		Mono<String> mono = Mono.fromSupplier(() -> {
									 if(counter.incrementAndGet() < 2) {
										 throw new RuntimeException("oops");
									 }
									 return "value";
								 });

		StepVerifier.withVirtualTime(() -> mono.retryWhen(reactor.util.retry.Retry.fixedDelay(3, Duration.ofSeconds(1))))
					.expectSubscription()
					.thenAwait(Duration.ofSeconds(1))
					.expectNext("value")
					.expectComplete()
					.verify();
	}
}
