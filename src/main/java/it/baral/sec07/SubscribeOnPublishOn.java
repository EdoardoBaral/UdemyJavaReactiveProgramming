package it.baral.sec07;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Dimostra la combinazione di {@code publishOn} e {@code subscribeOn} nella stessa
 * catena: il primo sposta l'esecuzione degli operatori a valle, il secondo quella
 * della sorgente, indipendentemente dalla posizione in cui viene dichiarato.
 */
public class SubscribeOnPublishOn {

	private static final Logger log = LoggerFactory.getLogger(SubscribeOnPublishOn.class);

	/**
	 * Costruisce un {@link Flux} con {@code publishOn(Schedulers.parallel())} seguito
	 * da {@code subscribeOn(Schedulers.boundedElastic())}, mostrando su quali thread
	 * vengono eseguite le diverse fasi della catena.
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
								 .subscribeOn(Schedulers.boundedElastic())
								 .doFirst(() -> log.info("first2"));
		
		Runnable runnable1 = () -> flux.subscribe(Util.subscriber("sub1"));
		Thread.ofPlatform().start(runnable1);
		
		Util.sleepSeconds(5);
	}
}
