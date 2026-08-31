package it.baral.sec11;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ClientError Tests")
class ClientErrorTest {

	@Test
	@DisplayName("ClientError espone il messaggio fisso \"bad request\"")
	void testClientErrorHasFixedMessage() {
		assertEquals("bad request", new ClientError().getMessage());
	}
}
