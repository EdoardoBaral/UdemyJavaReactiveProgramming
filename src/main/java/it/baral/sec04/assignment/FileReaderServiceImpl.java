package it.baral.sec04.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Implementazione di {@link FileReaderService} basata su {@code Flux.generate} con stato:
 * lo stato è il {@link BufferedReader} aperto sul file, condiviso tra le fasi di apertura,
 * lettura riga per riga e chiusura della risorsa.
 */
public class FileReaderServiceImpl implements FileReaderService{

	private static final Logger log = LoggerFactory.getLogger(FileReaderServiceImpl.class);

	/**
	 * Crea un {@code Flux} che apre il file, ne legge le righe una per una tramite
	 * il generatore con stato, e chiude la risorsa al termine.
	 *
	 * @param path percorso del file da leggere
	 * @return un {@code Flux} che emette una riga per ogni elemento
	 */
	@Override
	public Flux<String> readFile(Path path) {
		return Flux.generate(() -> openFile(path),
							 this::readFile,
							 this::closeFile);
	}

	/**
	 * Apre in lettura il file al percorso indicato, usato come stato iniziale del generatore.
	 *
	 * @param path percorso del file da aprire
	 * @return il {@link BufferedReader} aperto sul file
	 * @throws IOException se l'apertura del file fallisce
	 */
	private BufferedReader openFile(Path path) throws IOException {
		log.info("Opening file");
		return Files.newBufferedReader(path);
	}

	/**
	 * Legge la riga successiva dal reader ed emette il valore sul sink, oppure
	 * completa il flusso se il file è terminato, o propaga l'errore in caso di eccezione.
	 *
	 * @param reader lo stato corrente, ossia il reader aperto sul file
	 * @param sink il sink su cui emettere la riga letta, completare o segnalare un errore
	 * @return il reader, invariato, da riutilizzare come stato nella chiamata successiva
	 */
	private BufferedReader readFile(BufferedReader reader, SynchronousSink<String> sink) {
		try {
			String line =  reader.readLine();
			log.info("Reading line: " + line);
			if(line == null) {
				sink.complete();
			} else {
				sink.next(line);
			}
		} catch(Exception e) {
			sink.error(e);
		}

		return reader;
	}

	/**
	 * Chiude il reader al termine dell'emissione, come richiesto dalla firma
	 * a tre argomenti di {@code Flux.generate}.
	 *
	 * @param reader il reader da chiudere
	 * @throws RuntimeException se la chiusura del reader fallisce
	 */
	private void closeFile(BufferedReader reader) {
		try {
			reader.close();
			log.info("File closed");
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
