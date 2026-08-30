package it.baral.sec02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("FileServiceImpl Tests")
class FileServiceImplTest {

	private FileService fileService;
	private Path testDir;

	@BeforeEach
	void setUp() throws Exception {
		fileService = new FileServiceImpl();
		testDir = Paths.get("src/main/resources/sec02");
		Files.createDirectories(testDir);
	}

	@Test
	@DisplayName("write() crea un nuovo file con il contenuto specificato")
	void testWriteCreatesFile() throws Exception {
		String fileName = "test_write.txt";
		String content = "Test content";
		Path filePath = testDir.resolve(fileName);

		try {
			Mono<Void> result = fileService.write(fileName, content);

			StepVerifier.create(result)
						.expectComplete()
						.verify();

			assertTrue(Files.exists(filePath));
			String readContent = Files.readString(filePath);
			assertEquals(content, readContent);
		} finally {
			try {
				Files.deleteIfExists(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	@DisplayName("write() sovrascrive il contenuto di un file esistente")
	void testWriteOverwritesExistingFile() throws Exception {
		String fileName = "test_overwrite.txt";
		String originalContent = "Original content";
		String newContent = "New content";
		Path filePath = testDir.resolve(fileName);

		try {
			Mono<Void> result1 = fileService.write(fileName, originalContent);
			StepVerifier.create(result1)
						.expectComplete()
						.verify();

			Mono<Void> result2 = fileService.write(fileName, newContent);
			StepVerifier.create(result2)
						.expectComplete()
						.verify();

			String readContent = Files.readString(filePath);
			assertEquals(newContent, readContent);
		} finally {
			try {
				Files.deleteIfExists(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	@DisplayName("read() legge il contenuto di un file esistente")
	void testReadExistingFile() throws Exception {
		String fileName = "test_read.txt";
		String content = "Test read content";
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
	@DisplayName("read() propaga un errore se il file non esiste")
	void testReadNonExistentFile() {
		String fileName = "non_existent_file.txt";

		Mono<String> result = fileService.read(fileName);

		StepVerifier.create(result)
					.expectError()
					.verify();
	}

	@Test
	@DisplayName("delete() elimina un file esistente")
	void testDeleteExistingFile() throws Exception {
		String fileName = "test_delete.txt";
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
	@DisplayName("delete() propaga un errore se il file non esiste")
	void testDeleteNonExistentFile() {
		String fileName = "non_existent_delete.txt";

		Mono<Void> result = fileService.delete(fileName);

		StepVerifier.create(result)
					.expectError()
					.verify();
	}

	@Test
	@DisplayName("read() legge un file vuoto")
	void testReadEmptyFile() throws Exception {
		String fileName = "test_empty.txt";
		Path filePath = testDir.resolve(fileName);

		try {
			Files.writeString(filePath, "");

			Mono<String> result = fileService.read(fileName);

			StepVerifier.create(result)
						.expectNext("")
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
	@DisplayName("write() con contenuto multilinea")
	void testWriteMultilineContent() throws Exception {
		String fileName = "test_multiline.txt";
		String content = "Line 1\nLine 2\nLine 3";
		Path filePath = testDir.resolve(fileName);

		try {
			Mono<Void> result = fileService.write(fileName, content);

			StepVerifier.create(result)
						.expectComplete()
						.verify();

			String readContent = Files.readString(filePath);
			assertEquals(content, readContent);
		} finally {
			try {
				Files.deleteIfExists(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
