package it.baral.sec10.assignment.window;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Esercizio sull'operatore {@code window} (sezione sec10): suddivide un flusso continuo
 * di eventi in finestre di dimensione fissa e scrive ciascuna finestra su un file separato
 * tramite {@link FileWriter}.
 */
public class WindowAssignment {

	/**
	 * Punto di ingresso dell'esercizio: suddivide il flusso di eventi in finestre di 5 elementi
	 * e scrive ciascuna finestra su un file numerato progressivamente.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		AtomicInteger counter = new AtomicInteger(0);
		String fileNameFormat = "src/main/resources/sec10/file%d.txt";

		eventStream().window(5)
			.flatMap(flux -> FileWriter.create(flux, Path.of(fileNameFormat.formatted(counter.incrementAndGet()))))
			.subscribe();

		Util.sleepSeconds(60);
	}

	/**
	 * Genera un flusso infinito di eventi, uno ogni 200 millisecondi.
	 *
	 * @return un {@code Flux} di stringhe che rappresentano gli eventi generati
	 */
	private static Flux<String> eventStream() {
		return Flux.interval(Duration.ofMillis(200))
				   .map(i -> "event "+ (i+1));
	}
}
