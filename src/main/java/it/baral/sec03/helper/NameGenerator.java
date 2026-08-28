package it.baral.sec03.helper;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Classe di supporto della sezione 3 del corso: genera nomi completi
 * fittizi (con un'attesa simulata di un secondo per ciascuno), sia in forma
 * di {@link List} "eager" sia di {@link Flux} lazy, per confrontarne il
 * comportamento in {@link it.baral.sec03.FluxVSList}.
 */
public class NameGenerator {

	/**
	 * Genera in modo eager una lista di {@code count} nomi fittizi,
	 * attendendo il completamento di ogni generazione prima di restituire il
	 * risultato.
	 *
	 * @param count il numero di nomi da generare
	 * @return la lista dei nomi generati
	 */
	public static List<String> generateNamesList(int count) {
		return IntStream.rangeClosed(1, count)
				   		.mapToObj(x -> generateNames())
				   		.toList();
	}

	/**
	 * Genera in modo lazy un {@link Flux} di {@code count} nomi fittizi,
	 * emessi uno alla volta man mano che vengono generati.
	 *
	 * @param count il numero di nomi da generare
	 * @return un {@link Flux} che emettera' i nomi generati
	 */
	public static Flux<String> generateNamesFlux(int count) {
		return Flux.range(1, count)
				   .map(x -> generateNames());
	}

	/**
	 * Genera un singolo nome completo fittizio, simulando un'elaborazione
	 * lenta tramite un'attesa di un secondo.
	 *
	 * @return il nome fittizio generato
	 */
	private static String generateNames() {
		Util.sleepSeconds(1);
		return Util.faker().name().fullName();
	}
}
