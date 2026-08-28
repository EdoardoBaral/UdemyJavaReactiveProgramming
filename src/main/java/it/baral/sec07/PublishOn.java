package it.baral.sec07;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Dimostra l'effetto dell'operatore {@code publishOn}: sposta l'esecuzione degli
 * operatori a valle sullo scheduler indicato, a differenza di {@code subscribeOn}
 * che agisce sulla sorgente indipendentemente dalla posizione nella catena.
 */
public class PublishOn {

	private static final Logger log = LoggerFactory.getLogger(PublishOn.class);

	/**
	 * Costruisce un {@link Flux} con due {@code publishOn} in sequenza, mostrando
	 * che ciascuno sposta gli operatori successivi sul proprio scheduler.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
		Flux<Integer> flux = Flux.create(sink -> {
								 	for(int i=0; i<3; i++) {
								 		log.info("generating: {}", i);
								 		sink.next(i);
								 	}
								 	sink.complete();
								 })
								 .cast(Integer.class)
								 .publishOn(Schedulers.parallel())
								 .doOnNext(v -> log.info("value: {}", v))
								 .doFirst(() -> log.info("first1"))
								 .publishOn(Schedulers.boundedElastic())
								 .doFirst(() -> log.info("first2"));
		
		Runnable runnable1 = () -> flux.subscribe(Util.subscriber("sub1"));
		Thread.ofPlatform().start(runnable1);
		
		Util.sleepSeconds(5);
	}
}
