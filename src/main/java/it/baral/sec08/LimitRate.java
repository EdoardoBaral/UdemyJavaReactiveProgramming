package it.baral.sec08;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Dimostra l'operatore {@code limitRate}, che riduce la quantit&agrave; di elementi
 * richiesti alla sorgente per ogni ciclo di richiesta, limitando la pressione sul
 * produttore rispetto alla richiesta illimitata di default del subscriber.
 */
public class LimitRate {

	private static final Logger log = LoggerFactory.getLogger(LimitRate.class);

	/**
	 * Genera interi in sequenza su uno scheduler parallelo, applica
	 * {@code limitRate(5)} e li elabora lentamente su {@code boundedElastic},
	 * mostrando le richieste effettuate a piccoli lotti.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
		Flux<Integer> producer = Flux.generate(() -> 1,
											   (state, sink) -> {
													log.info("generating: {}", state);
													sink.next(state);
													return ++state;
											   })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.limitRate(5)
				.publishOn(Schedulers.boundedElastic())
				.map(LimitRate::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	/**
	 * Simula un'elaborazione dispendiosa in tempo (1 secondo) sul valore ricevuto.
	 *
	 * @param i il valore da elaborare
	 * @return lo stesso valore ricevuto in ingresso
	 */
	private static int timeConsumingTask(int i) {
		log.info("{}", i);
		Util.sleepSeconds(1);
		return i;
	}
}
