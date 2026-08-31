package it.baral.sec11;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ServerError Tests")
class ServerErrorTest {

	@Test
	@DisplayName("ServerError espone il messaggio fisso \"server error\"")
	void testServerErrorHasFixedMessage() {
		assertEquals("server error", new ServerError().getMessage());
	}
}
