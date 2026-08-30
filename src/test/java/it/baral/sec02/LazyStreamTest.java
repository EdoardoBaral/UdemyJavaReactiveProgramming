package it.baral.sec02;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("LazyStream Tests")
class LazyStreamTest {

	@Test
	@DisplayName("Stream senza operazione terminale non esegue le operazioni intermedie")
	void testStreamWithoutTerminalOperation() {
		boolean[] peekExecuted = {false};

		Stream.of(1, 2, 3)
			  .peek(i -> peekExecuted[0] = true);

		assertFalse(peekExecuted[0], "peek deve essere lazy e non eseguito senza operazione terminale");
	}

	@Test
	@DisplayName("Stream con toList esegue le operazioni intermedie")
	void testStreamWithTerminalOperation() {
		boolean[] peekExecuted = {false};

		Stream.of(1, 2, 3)
			  .peek(i -> peekExecuted[0] = true)
			  .toList();

		assertTrue(peekExecuted[0], "peek deve essere eseguito quando viene chiamata una operazione terminale");
	}

	@Test
	@DisplayName("toList() ritorna una lista con tutti gli elementi")
	void testStreamToList() {
		List<Integer> result = Stream.of(1, 2, 3)
									 .toList();

		assertEquals(3, result.size());
		assertTrue(result.contains(1));
		assertTrue(result.contains(2));
		assertTrue(result.contains(3));
	}

	@Test
	@DisplayName("Stream con filter e toList ritorna solo gli elementi filtrati")
	void testStreamWithFilter() {
		List<Integer> result = Stream.of(1, 2, 3, 4, 5)
									 .filter(i -> i % 2 == 0)
									 .toList();

		assertEquals(2, result.size());
		assertTrue(result.contains(2));
		assertTrue(result.contains(4));
	}

	@Test
	@DisplayName("Stream con map e toList ritorna elementi trasformati")
	void testStreamWithMap() {
		List<Integer> result = Stream.of(1, 2, 3)
									 .map(i -> i * 2)
									 .toList();

		assertEquals(3, result.size());
		assertEquals(2, result.get(0));
		assertEquals(4, result.get(1));
		assertEquals(6, result.get(2));
	}

	@Test
	@DisplayName("Stream vuoto ritorna una lista vuota")
	void testEmptyStream() {
		List<Integer> result = Stream.<Integer>of()
									 .toList();

		assertTrue(result.isEmpty());
	}

	@Test
	@DisplayName("Stream con un solo elemento ritorna una lista con un elemento")
	void testStreamWithSingleElement() {
		List<String> result = Stream.of("single")
									.toList();

		assertEquals(1, result.size());
		assertEquals("single", result.getFirst());
	}

	@Test
	@DisplayName("peek conta le volte che viene eseguito")
	void testPeekExecutionCount() {
		int[] count = {0};

		Stream.of(1, 2, 3)
			  .peek(i -> count[0]++)
			  .toList();

		assertEquals(3, count[0], "peek deve essere eseguito per ogni elemento");
	}

	@Test
	@DisplayName("Stream con flatMap espande gli elementi")
	void testStreamWithFlatMap() {
		List<Integer> result = Stream.of(1, 2, 3)
									 .flatMap(i -> Stream.of(i, i * 10))
									 .toList();

		assertEquals(6, result.size());
		assertEquals(1, result.get(0));
		assertEquals(10, result.get(1));
		assertEquals(2, result.get(2));
		assertEquals(20, result.get(3));
		assertEquals(3, result.get(4));
		assertEquals(30, result.get(5));
	}
}
