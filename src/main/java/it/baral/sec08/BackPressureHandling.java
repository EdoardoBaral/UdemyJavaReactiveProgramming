package it.baral.sec08;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Dimostra il meccanismo di backpressure di default di Reactor: un produttore veloce
 * ({@code Flux.generate}) e un consumatore lento, con il buffer interno di richiesta
 * ridotto tramite la property {@code reactor.bufferSize.small}.
 */
public class BackPressureHandling {

	private static final Logger log = LoggerFactory.getLogger(BackPressureHandling.class);

	/**
	 * Genera interi in sequenza su uno scheduler parallelo e li elabora con
	 * un'operazione dispendiosa in tempo su {@code boundedElastic}, mostrando come
	 * il meccanismo di richiesta (request-n) regoli automaticamente il produttore.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
		System.setProperty("reactor.bufferSize.small", "16");
		
		Flux<Integer> producer = Flux.generate(() -> 1,
											   (state, sink) -> {
												   log.info("generating: {}", state);
												   sink.next(state);
												   return ++state;
											   })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.publishOn(Schedulers.boundedElastic())
				.map(BackPressureHandling::timeConsumingTask)
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
		Util.sleepSeconds(1);
		return i;
	}
}
