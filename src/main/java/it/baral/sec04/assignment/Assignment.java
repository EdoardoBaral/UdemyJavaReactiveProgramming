package it.baral.sec04.assignment;

import it.baral.common.Util;

import java.nio.file.Path;

/**
 * Classe di esercitazione che usa {@link FileReaderServiceImpl} per leggere un file
 * di esempio riga per riga tramite {@code Flux.generate}.
 */
public class Assignment {

	/**
	 * Legge il file di esempio e ne sottoscrive le righe emesse.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
	
		Path path = Path.of("src/main/resources/sec04/file.txt");
		FileReaderService fileReaderService = new FileReaderServiceImpl();
		fileReaderService.readFile(path)
//						 .take(10)
//						 .takeUntil(l -> l.equalsIgnoreCase("Riga 010"))
						 .subscribe(Util.subscriber());
	}
}
