package it.baral.sec02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DisplayName("MonoEmptyError Tests")
class MonoEmptyErrorTest {

	@Test
	@DisplayName("getUsername() ritorna valore per userId=1")
	void testGetUsernameReturnsValue() {
		Mono<String> result = getUsername(1);

		StepVerifier.create(result)
					.expectNext("Edoardo")
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("getUsername() ritorna Mono vuoto per userId=2")
	void testGetUsernameReturnsEmpty() {
		Mono<String> result = getUsername(2);

		StepVerifier.create(result)
					.expectComplete()
					.verify();
	}

	@Test
	@DisplayName("getUsername() ritorna Mono in errore per userId invalido")
	void testGetUsernameReturnsError() {
		Mono<String> result = getUsername(3);

		StepVerifier.create(result)
					.expectError(IllegalArgumentException.class)
					.verify();
	}

	@Test
	@DisplayName("getUsername() solleva IllegalArgumentException per userId=999")
	void testGetUsernameThrowsExceptionForInvalidId() {
		Mono<String> result = getUsername(999);

		StepVerifier.create(result)
					.expectErrorMessage("Invalid input")
					.verify();
	}

	@Test
	@DisplayName("getUsername() non emette valori per userId invalido")
	void testGetUsernameEmitsNoValuesForError() {
		Mono<String> result = getUsername(0);

		StepVerifier.create(result)
					.expectError()
					.verify();
	}

	private static Mono<String> getUsername(int userId) {
		return switch(userId) {
			case 1 -> Mono.just("Edoardo");
			case 2 -> Mono.empty();
			default -> Mono.error(new IllegalArgumentException("Invalid input"));
		};
	}
}
