package it.baral.sec10.assignment.buffer;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Esercizio sull'operatore {@code buffer} (sezione sec10): filtra un flusso continuo
 * di ordini di libri per genere consentito e genera periodicamente un report dei ricavi
 * raggruppati per genere.
 */
public class BufferAssignment {

	/**
	 * Punto di ingresso dell'esercizio: filtra gli ordini per genere consentito e genera
	 * un report dei ricavi ogni 5 secondi.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Set<String> allowedCategories = Set.of("Science fiction", "Fantasy", "Suspense/Thriller");

		orderStream().filter(o -> allowedCategories.contains(o.genre()))
			.buffer(Duration.ofSeconds(5))
			.map(BufferAssignment::generateReport)
			.subscribe(Util.subscriber());

		Util.sleepSeconds(60);
	}

	/**
	 * Genera un flusso infinito di ordini di libri casuali, uno ogni 200 millisecondi.
	 *
	 * @return un {@code Flux} di {@link BookOrder} generati casualmente
	 */
	private static Flux<BookOrder> orderStream() {
		return Flux.interval(Duration.ofMillis(200))
				   .map(i -> BookOrder.create());
	}

	/**
	 * Genera un {@link RevenueReport} aggregando i ricavi di una lista di ordini per genere.
	 *
	 * @param orders lista degli ordini raccolti nel periodo corrente
	 * @return il report dei ricavi per genere relativo agli ordini forniti
	 */
	private static RevenueReport generateReport(List<BookOrder> orders) {
		Map<String, Integer> revenue = orders.stream()
									   		 .collect(Collectors.groupingBy(BookOrder::genre, Collectors.summingInt(BookOrder::price)));
		return new RevenueReport(LocalDateTime.now(), revenue);
	}
}
