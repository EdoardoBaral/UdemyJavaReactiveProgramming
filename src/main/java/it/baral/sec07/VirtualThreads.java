package it.baral.sec07;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Dimostra l'uso dei virtual thread di Java come backing per lo scheduler
 * {@code boundedElastic} di Reactor, abilitato tramite la property di sistema
 * {@code reactor.schedulers.defaultBoundedElasticOnVirtualThreads}.
 */
public class VirtualThreads {

	private static final Logger log = LoggerFactory.getLogger(VirtualThreads.class);

	/**
	 * Abilita i virtual thread per lo scheduler {@code boundedElastic}, esegue un
	 * {@link Flux} con {@code subscribeOn(Schedulers.boundedElastic())} e verifica
	 * (tramite {@code Thread.currentThread().isVirtual()}) che l'esecuzione avvenga
	 * su un virtual thread.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
		System.setProperty("reactor.schedulers.defaultBoundedElasticOnVirtualThreads", "true");
		Flux<Integer> flux = Flux.create(sink -> {
								 	for(int i=0; i<3; i++) {
								 		log.info("generating: {}", i);
								 		sink.next(i);
								 	}
								 	sink.complete();
								 })
								 .cast(Integer.class)
								 .doOnNext(v -> log.info("value: {}", v))
								 .doFirst(() -> log.info("first1-{}", Thread.currentThread().isVirtual()))
								 .subscribeOn(Schedulers.boundedElastic())
								 .doFirst(() -> log.info("first2"));
		
		Runnable runnable1 = () -> flux.subscribe(Util.subscriber("sub1"));
		Thread.ofPlatform().start(runnable1);
		
		Util.sleepSeconds(5);
	}
}
