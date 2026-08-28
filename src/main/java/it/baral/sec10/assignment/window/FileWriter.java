package it.baral.sec10.assignment.window;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Scrittore di file di supporto per l'esercizio sull'operatore {@code window} (sezione sec10):
 * scrive su file, riga per riga, gli elementi emessi da una finestra di un {@link Flux}.
 */
@RequiredArgsConstructor
public class FileWriter {

	private final Path path;

	private BufferedWriter writer;

	/**
	 * Apre il file in scrittura al percorso configurato, creando il {@link BufferedWriter} interno.
	 *
	 * @throws RuntimeException se si verifica un errore di I/O durante l'apertura del file
	 */
	private void createFile() {
		try {
			this.writer = Files.newBufferedWriter(path);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Chiude il file precedentemente aperto in scrittura.
	 *
	 * @throws RuntimeException se si verifica un errore di I/O durante la chiusura del file
	 */
	private void closeFile() {
		try {
			this.writer.close();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Scrive una riga di contenuto sul file e forza lo scaricamento del buffer.
	 *
	 * @param content contenuto della riga da scrivere
	 * @throws RuntimeException se si verifica un errore di I/O durante la scrittura
	 */
	private void write(String content) {
		try {
			this.writer.write(content);
			this.writer.newLine();
			this.writer.flush();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Crea un nuovo file al percorso indicato e vi scrive tutti gli elementi emessi
	 * dal {@link Flux} fornito, aprendo il file all'avvio della sottoscrizione
	 * e chiudendolo al termine (con successo o errore).
	 *
	 * @param flux flusso di stringhe da scrivere sul file
	 * @param path percorso del file di destinazione
	 * @return un {@code Mono<Void>} che completa quando la scrittura è terminata
	 */
	public static Mono<Void> create(Flux<String> flux, Path path) {
		FileWriter fileWriter = new FileWriter(path);
		return flux.doOnNext(fileWriter::write)
				   .doFirst(fileWriter::createFile)
				   .doFinally(s -> fileWriter.closeFile())
				   .then();
	}
}
