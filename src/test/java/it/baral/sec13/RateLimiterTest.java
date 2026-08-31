package it.baral.sec13;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

@DisplayName("RateLimiter Tests")
class RateLimiterTest {

	@Test
	@DisplayName("limitCalls() fallisce quando il Context non contiene la chiave \"category\"")
	void testLimitCallsFailsWhenCategoryKeyIsMissingFromContext() {
		StepVerifier.create(RateLimiter.<Void>limitCalls())
					.expectError(RuntimeException.class)
					.verify();
	}

	@Test
	@DisplayName("limitCalls() fallisce quando la categoria indicata nel Context non e' mai stata caricata di tentativi")
	void testLimitCallsFailsWhenCategoryIsUnknown() {
		StepVerifier.create(RateLimiter.<Void>limitCalls()
										.contextWrite(reactor.util.context.Context.of("category", "unknown-test-category")))
					.expectError(RuntimeException.class)
					.verify();
	}

	@Test
	@DisplayName("limitCalls() consente le chiamate finche' restano tentativi disponibili per la categoria, poi fallisce una volta esauriti")
	void testLimitCallsAllowsCallsWhileAttemptsRemainAndFailsOnceExhausted() {
		reactor.util.context.Context standardContext = reactor.util.context.Context.of("category", "standard");

		StepVerifier.create(RateLimiter.<Void>limitCalls().contextWrite(standardContext))
					.expectComplete()
					.verify();

		StepVerifier.create(RateLimiter.<Void>limitCalls().contextWrite(standardContext))
					.expectComplete()
					.verify();

		StepVerifier.create(RateLimiter.<Void>limitCalls().contextWrite(standardContext))
					.expectError(RuntimeException.class)
					.verify();
	}
}
