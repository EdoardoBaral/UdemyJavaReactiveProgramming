package it.baral.sec07;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Dimostra l'effetto di pi&ugrave; chiamate a {@code subscribeOn} sulla stessa catena
 * reattiva: solo la prima (quella pi&ugrave; vicina alla sorgente) determina lo scheduler
 * su cui viene eseguita la generazione dei valori.
 */
public class MultipleSubscribeOn {

	private static final Logger log = LoggerFactory.getLogger(MultipleSubscribeOn.class);

	/**
	 * Punto di ingresso: seleziona quale scenario dimostrativo eseguire.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
//		boundedElasticExecution();
//		newParallelExecution();
		immediateExecution();
	}

	/**
	 * Esegue la catena con due {@code subscribeOn} annidati, entrambi su
	 * {@code Schedulers.boundedElastic()}, mostrando che vince il primo applicato.
	 */
	private static void boundedElasticExecution() {
		Flux<Integer> flux = Flux.create(sink -> {
								 	for(int i=0; i<3; i++) {
								 		log.info("generating: {}", i);
								 		sink.next(i);
								 	}
								 	sink.complete();
								 })
								 .cast(Integer.class)
								 .subscribeOn(Schedulers.boundedElastic())
								 .doOnNext(v -> log.info("value: {}", v))
								 .doFirst(() -> log.info("first1"))
								 .subscribeOn(Schedulers.boundedElastic())
								 .doFirst(() -> log.info("first2"));
		
		Runnable runnable1 = () -> flux.subscribe(Util.subscriber("sub1"));
		Thread.ofPlatform().start(runnable1);
		
		Util.sleepSeconds(5);
	}
	
	/**
	 * Esegue la catena con il primo {@code subscribeOn} su uno scheduler parallelo
	 * dedicato ({@code Schedulers.newParallel("parallel")}) e il secondo su
	 * {@code boundedElastic}, mostrando che vince comunque il primo applicato.
	 */
	private static void newParallelExecution() {
		Flux<Integer> flux = Flux.create(sink -> {
								 	for(int i=0; i<3; i++) {
								 		log.info("generating: {}", i);
								 		sink.next(i);
								 	}
								 	sink.complete();
								 })
								 .cast(Integer.class)
								 .subscribeOn(Schedulers.newParallel("parallel"))
								 .doOnNext(v -> log.info("value: {}", v))
								 .doFirst(() -> log.info("first1"))
								 .subscribeOn(Schedulers.boundedElastic())
								 .doFirst(() -> log.info("first2"));
		
		Runnable runnable1 = () -> flux.subscribe(Util.subscriber("sub1"));
		Thread.ofPlatform().start(runnable1);
		
		Util.sleepSeconds(5);
	}
	
	/**
	 * Esegue la catena con il primo {@code subscribeOn} su {@code Schedulers.immediate()},
	 * mostrando che con questo scheduler la generazione avviene sul thread chiamante,
	 * come se {@code subscribeOn} non fosse presente.
	 */
	private static void immediateExecution() {
		Flux<Integer> flux = Flux.create(sink -> {
								 	for(int i=0; i<3; i++) {
								 		log.info("generating: {}", i);
								 		sink.next(i);
								 	}
								 	sink.complete();
								 })
								 .cast(Integer.class)
								 .subscribeOn(Schedulers.immediate())
								 .doOnNext(v -> log.info("value: {}", v))
								 .doFirst(() -> log.info("first1"))
//								 .subscribeOn(Schedulers.boundedElastic())
								 .doFirst(() -> log.info("first2"));
		
		Runnable runnable1 = () -> flux.subscribe(Util.subscriber("sub1"));
		Thread.ofPlatform().start(runnable1);
		
		Util.sleepSeconds(5);
	}
}
