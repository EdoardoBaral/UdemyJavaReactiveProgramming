package it.baral.sec08;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * Dimostra le diverse strategie di gestione della backpressure disponibili per un
 * {@link Flux} costruito con {@code Flux.create} quando il produttore supera la
 * capacit&agrave; di richiesta del consumatore: buffer illimitato, buffer di
 * dimensione fissa, errore, drop e mantenimento dell'ultimo valore.
 */
public class BackPressureStrategies {

	private static final Logger log = LoggerFactory.getLogger(BackPressureStrategies.class);

	/**
	 * Punto di ingresso: seleziona quale strategia di backpressure dimostrare.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
//		bufferStrategy();
//		errorStrategy();
//		bufferSizeStrategy();
//		dropStrategy();
//		lastStrategy();
		overflowStrategy();
	}

	/**
	 * Simula un'elaborazione dispendiosa in tempo (1 secondo) sul valore ricevuto.
	 *
	 * @param i il valore da elaborare
	 * @return lo stesso valore ricevuto in ingresso
	 */
	private static int timeConsumingTask(int i) {
		log.info("received: {}", i);
		Util.sleepSeconds(1);
		return i;
	}

	/**
	 * Applica {@code onBackpressureBuffer()} (buffer illimitato) per accumulare
	 * gli elementi in eccesso in attesa che il consumatore li richieda.
	 */
	private static void bufferStrategy() {
		Flux<Integer> producer = Flux.create((sink) -> {
									 		 	for(int i=1; i<=500 && !sink.isCancelled(); i++) {
									 		 		log.info("generating: {}", i);
									 		 		Util.sleep(Duration.ofMillis(50));
									 		 		sink.next(i);
									 		 	}
									 		 	sink.complete();
									 		 })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.onBackpressureBuffer()
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	/**
	 * Applica {@code onBackpressureError()}, che termina il flusso con un errore
	 * non appena il consumatore non riesce a tenere il passo del produttore.
	 */
	private static void errorStrategy() {
		Flux<Integer> producer = Flux.create((sink) -> {
									 		 	for(int i=1; i<=500 && !sink.isCancelled(); i++) {
									 		 		log.info("generating: {}", i);
									 		 		Util.sleep(Duration.ofMillis(50));
									 		 		sink.next(i);
									 		 	}
									 		 	sink.complete();
									 		 })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.onBackpressureError()
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	/**
	 * Applica {@code onBackpressureBuffer(10)}, un buffer di dimensione fissa (10
	 * elementi) oltre la quale il flusso termina con un errore di overflow.
	 */
	private static void bufferSizeStrategy() {
		Flux<Integer> producer = Flux.create((sink) -> {
									 		 	for(int i=1; i<=500 && !sink.isCancelled(); i++) {
									 		 		log.info("generating: {}", i);
									 		 		Util.sleep(Duration.ofMillis(50));
									 		 		sink.next(i);
									 		 	}
									 		 	sink.complete();
									 		 })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.onBackpressureBuffer(10)
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	/**
	 * Applica {@code onBackpressureDrop()}, che scarta silenziosamente gli elementi
	 * in eccesso non ancora richiesti dal consumatore.
	 */
	private static void dropStrategy() {
		Flux<Integer> producer = Flux.create((sink) -> {
												  for(int i=1; i<=500 && !sink.isCancelled(); i++) {
												 	 log.info("generating: {}", i);
												 	 Util.sleep(Duration.ofMillis(50));
												 	 sink.next(i);
												  }
												  sink.complete();
											 })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.onBackpressureDrop()
				.log()
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	/**
	 * Applica {@code onBackpressureLatest()}, che mantiene in memoria solo l'ultimo
	 * elemento emesso quando il consumatore non riesce a tenere il passo, scartando
	 * i precedenti non ancora richiesti.
	 */
	private static void lastStrategy() {
		Flux<Integer> producer = Flux.create((sink) -> {
									 		 	for(int i=1; i<=500 && !sink.isCancelled(); i++) {
									 		 		log.info("generating: {}", i);
									 		 		Util.sleep(Duration.ofMillis(50));
									 		 		sink.next(i);
									 		 	}
									 		 	sink.complete();
									 		 })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.onBackpressureLatest()
				.log()
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	/**
	 * Imposta direttamente la strategia di overflow ({@code FluxSink.OverflowStrategy.BUFFER})
	 * al momento della creazione del {@link Flux} con {@code Flux.create}, in
	 * alternativa all'uso degli operatori {@code onBackpressureXxx}.
	 */
	private static void overflowStrategy() {
		Flux<Integer> producer = Flux.create((sink) -> {
									 		 	for(int i=1; i<=500 && !sink.isCancelled(); i++) {
									 		 		log.info("generating: {}", i);
									 		 		Util.sleep(Duration.ofMillis(50));
									 		 		sink.next(i);
									 		 	}
									 		 	sink.complete();
									 		 },
									 		 FluxSink.OverflowStrategy.BUFFER)
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.log()
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
}
