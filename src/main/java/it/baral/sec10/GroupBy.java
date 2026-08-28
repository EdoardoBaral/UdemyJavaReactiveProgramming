package it.baral.sec10;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Dimostra l'operatore {@code groupBy}: suddivide un {@link Flux} in più {@link GroupedFlux}
 * (sotto-flussi associati a una chiave) in base a una funzione di classificazione.
 */
public class GroupBy {

	private static final Logger log = LoggerFactory.getLogger(GroupBy.class);

	/**
	 * Punto di ingresso: esegue la demo di raggruppamento su numeri pari/dispari
	 * applicata a numeri già raddoppiati (la variante con tutti i numeri è disponibile
	 * ma commentata).
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		groupByWithAllNumbers();
		groupByWithOnlyEvenNumbers();

		Util.sleepSeconds(60);
	}

	/**
	 * Raggruppa i numeri da 1 a 30 in due gruppi (pari/dispari) in base al resto della divisione per 2.
	 */
	private static void groupByWithAllNumbers() {
		Flux.range(1, 30)
			.delayElements(Duration.ofSeconds(1))
			.groupBy(i -> i % 2 == 0) //In questo modo la chiave di raggruppamento sarà Boolean. Potrei anche specificare i%2 e farla diventare Integer, è indifferente
			.flatMap(GroupBy::processEvents)
			.subscribe();
	}

	/**
	 * Raggruppa i numeri da 1 a 30, prima raddoppiati, in base al resto della divisione per 2
	 * (producendo di fatto un unico gruppo, poiché tutti i valori raddoppiati sono pari).
	 */
	private static void groupByWithOnlyEvenNumbers() {
		Flux.range(1, 30)
			.delayElements(Duration.ofSeconds(1))
			.map(i -> i * 2)
			.groupBy(i -> i % 2 == 0)
			.flatMap(GroupBy::processEvents)
			.subscribe();
	}

	/**
	 * Elabora un singolo sotto-flusso raggruppato, tracciandone la chiave, ogni elemento emesso
	 * e il completamento tramite logger.
	 *
	 * @param groupedFlux sotto-flusso raggruppato da elaborare
	 * @return un {@code Mono<Void>} che completa quando il sotto-flusso è stato elaborato interamente
	 */
	private static Mono<Void> processEvents(GroupedFlux<Boolean, Integer> groupedFlux) {
		log.info("recieived flux for {}", groupedFlux.key());
		return groupedFlux.doOnNext(i -> log.info("key: {}, value: {}", groupedFlux.key(), i))
						  .doOnComplete(() -> log.info("{} completed", groupedFlux.key()))
						  .then();
	}
}
