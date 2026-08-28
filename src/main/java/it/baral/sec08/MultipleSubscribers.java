package it.baral.sec08;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Dimostra come pi&ugrave; subscriber indipendenti sullo stesso {@link Flux} freddo
 * (senza condivisione tramite {@code share}/{@code cache}) ricevano ciascuno una
 * propria esecuzione della sorgente, con velocit&agrave; di richiesta indipendenti.
 */
public class MultipleSubscribers {

	private static final Logger log = LoggerFactory.getLogger(MultipleSubscribers.class);

	/**
	 * Sottoscrive due subscriber allo stesso {@link Flux} generato: il primo limita
	 * la velocit&agrave; di richiesta con {@code limitRate} ed elabora ogni valore
	 * lentamente, il secondo consuma solo i primi 100 valori senza limitazioni.
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
				.map(MultipleSubscribers::timeConsumingTask)
				.subscribe(Util.subscriber("sub1"));
		
		producer.take(100)
				.publishOn(Schedulers.boundedElastic())
				.subscribe(Util.subscriber("sub2"));
		
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
