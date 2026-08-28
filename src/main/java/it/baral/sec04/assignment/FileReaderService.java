package it.baral.sec04.assignment;

import reactor.core.publisher.Flux;

import java.nio.file.Path;

/**
 * Interfaccia di un servizio che legge un file riga per riga in modo reattivo,
 * usata come esercizio sull'operatore {@code Flux.generate} con stato.
 */
public interface FileReaderService {

	/**
	 * Legge il file al percorso indicato ed emette una riga di testo alla volta.
	 *
	 * @param path percorso del file da leggere
	 * @return un {@code Flux} che emette una riga per ogni elemento, in errore se la lettura fallisce
	 */
	Flux<String> readFile(Path path);
}
