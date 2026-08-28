package it.baral.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implementazione di {@link FileService} basata sull'API {@link Files} di
 * Java NIO: incapsula le operazioni di I/O bloccanti in {@link Mono}
 * (tramite {@code fromCallable}/{@code fromRunnable}), operando sui file
 * contenuti nella cartella {@code src/main/resources/sec02}.
 */
public class FileServiceImpl implements FileService {

	private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
	private static final Path PATH = Path.of("src/main/resources/sec02");

	/**
	 * Legge il contenuto testuale del file indicato dalla cartella di lavoro.
	 *
	 * @param fileName il nome del file da leggere
	 * @return un {@link Mono} con il contenuto del file
	 */
	@Override
	public Mono<String> read(String fileName) {
		return Mono.fromCallable(() -> Files.readString(PATH.resolve(fileName)));
	}

	/**
	 * Scrive il contenuto indicato nel file specificato, nella cartella di
	 * lavoro.
	 *
	 * @param fileName il nome del file su cui scrivere
	 * @param content il contenuto testuale da scrivere nel file
	 * @return un {@link Mono} vuoto che completa a scrittura avvenuta
	 */
	@Override
	public Mono<Void> write(String fileName, String content) {
		return Mono.fromRunnable(() -> this.writeFile(fileName, content));
	}

	/**
	 * Elimina il file indicato dalla cartella di lavoro.
	 *
	 * @param fileName il nome del file da eliminare
	 * @return un {@link Mono} vuoto che completa a eliminazione avvenuta
	 */
	@Override
	public Mono<Void> delete(String fileName) {
		return Mono.fromRunnable(() -> this.deleteFile(fileName));
	}

	/**
	 * Scrive fisicamente il contenuto nel file indicato, incapsulando
	 * un'eventuale eccezione di I/O in una {@link RuntimeException}.
	 *
	 * @param fileName il nome del file su cui scrivere
	 * @param content il contenuto testuale da scrivere nel file
	 */
	private void writeFile(String fileName, String content) {
		try {
			Files.writeString(PATH.resolve(fileName), content);
			log.info("Created {}", fileName);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Elimina fisicamente il file indicato, incapsulando un'eventuale
	 * eccezione di I/O in una {@link RuntimeException}.
	 *
	 * @param fileName il nome del file da eliminare
	 */
	private void deleteFile(String fileName) {
		try {
			Files.delete(PATH.resolve(fileName));
			log.info("Deleted {}", fileName);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
