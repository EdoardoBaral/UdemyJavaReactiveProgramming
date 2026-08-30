package it.baral.sec02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("FileServiceAssignment Tests")
class FileServiceAssignmentTest {

	private FileService fileService;
	private Path testDir;

	@BeforeEach
	void setUp() throws Exception {
		fileService = new FileServiceImpl();
		testDir = Paths.get("src/main/resources/sec02");
		Files.createDirectories(testDir);
	}

	@Test
	@DisplayName("Scrivere un file tramite FileService funziona correttamente")
	void testWriteFileOperation() {
		String fileName = "assignment_test.txt";
		String content = "Assignment test content";
		Path filePath = testDir.resolve(fileName);

		try {
			Mono<Void> result = fileService.write(fileName, content);

			StepVerifier.create(result)
						.expectComplete()
						.verify();

			assertTrue(Files.exists(filePath));
		} finally {
			try {
				Files.deleteIfExists(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	@DisplayName("Leggere un file tramite FileService ritorna il contenuto corretto")
	void testReadFileOperation() throws Exception {
		String fileName = "assignment_read.txt";
		String content = "Content for assignment";
		Path filePath = testDir.resolve(fileName);

		try {
			Files.writeString(filePath, content);

			Mono<String> result = fileService.read(fileName);

			StepVerifier.create(result)
						.expectNext(content)
						.expectComplete()
						.verify();
		} finally {
			try {
				Files.deleteIfExists(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	@DisplayName("Eliminare un file tramite FileService rimuove il file")
	void testDeleteFileOperation() throws Exception {
		String fileName = "assignment_delete.txt";
		Path filePath = testDir.resolve(fileName);

		try {
			Files.writeString(filePath, "Content to delete");
			assertTrue(Files.exists(filePath));

			Mono<Void> result = fileService.delete(fileName);

			StepVerifier.create(result)
						.expectComplete()
						.verify();

			assertFalse(Files.exists(filePath));
		} finally {
			try {
				Files.deleteIfExists(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	@DisplayName("Scrivere e leggere lo stesso file mantiene il contenuto")
	void testWriteAndReadSameFile() throws Exception {
		String fileName = "assignment_write_read.txt";
		String content = "Write and read test";
		Path filePath = testDir.resolve(fileName);

		try {
			Mono<Void> writeResult = fileService.write(fileName, content);
			StepVerifier.create(writeResult)
						.expectComplete()
						.verify();

			Mono<String> readResult = fileService.read(fileName);
			StepVerifier.create(readResult)
						.expectNext(content)
						.expectComplete()
						.verify();
		} finally {
			try {
				Files.deleteIfExists(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	@DisplayName("Scrivere, leggere e eliminare file sequenzialmente")
	void testWriteReadDeleteSequentially() {
		String fileName = "assignment_sequence.txt";
		String content = "Sequential test content";
		Path filePath = testDir.resolve(fileName);

		try {
			Mono<Void> write = fileService.write(fileName, content);
			StepVerifier.create(write).expectComplete().verify();

			Mono<String> read = fileService.read(fileName);
			StepVerifier.create(read).expectNext(content).expectComplete().verify();

			Mono<Void> delete = fileService.delete(fileName);
			StepVerifier.create(delete).expectComplete().verify();

			assertFalse(Files.exists(filePath));
		} finally {
			try {
				Files.deleteIfExists(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	@DisplayName("Scrivere contenuto con caratteri speciali")
	void testWriteSpecialCharacters() {
		String fileName = "assignment_special.txt";
		String content = "Special chars: @#$%^&*()_+-=[]{}|;:',.<>?/\\";
		Path filePath = testDir.resolve(fileName);

		try {
			Mono<Void> result = fileService.write(fileName, content);
			StepVerifier.create(result).expectComplete().verify();

			Mono<String> readResult = fileService.read(fileName);
			StepVerifier.create(readResult)
						.expectNext(content)
						.expectComplete()
						.verify();
		} finally {
			try {
				Files.deleteIfExists(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
