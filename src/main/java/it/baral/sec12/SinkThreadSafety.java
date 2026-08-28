package it.baral.sec12;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Classe dimostrativa sulla thread-safety dei sink Reactor: confronta
 * l'emissione concorrente tramite {@code tryEmitNext}, che non garantisce
 * la serializzazione dei segnali, con {@code emitNext}, che invece
 * gestisce in modo sicuro le emissioni provenienti da più thread.
 */
public class SinkThreadSafety {

	private static final Logger log = LoggerFactory.getLogger(SinkThreadSafety.class);

	/**
	 * Punto di ingresso dell'applicazione. Decommentare uno dei metodi
	 * di demo per eseguire il relativo scenario.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		demoSinkThreadUnsafe();
		demoSinkThreadSafe();
	}

	/**
	 * Emette 1000 elementi da thread concorrenti diversi tramite
	 * {@code tryEmitNext}, mostrando come la mancata serializzazione possa
	 * causare la perdita di alcuni elementi (dimensione della lista finale
	 * inferiore a 1000).
	 */
	private static void demoSinkThreadUnsafe() {
		Sinks.Many<Integer> sink = Sinks.many()
									    .unicast()
									    .onBackpressureBuffer();
		Flux<Integer> flux = sink.asFlux();
		
		List<Integer> list = new ArrayList<>();
		flux.subscribe(list::add);
		
		for(int i=0; i<1000; i++) {
			int j = i;
			CompletableFuture.runAsync(() -> sink.tryEmitNext(j));
		}
		
		Util.sleepSeconds(5);
		log.info("list size: {}", list.size());
	}
	
	/**
	 * Emette 1000 elementi da thread concorrenti diversi tramite
	 * {@code emitNext} con un {@link Sinks.EmitFailureHandler} che ritenta
	 * l'emissione in caso di conflitto non serializzato, garantendo così
	 * che tutti gli elementi vengano effettivamente emessi.
	 */
	private static void demoSinkThreadSafe() {
		Sinks.Many<Integer> sink = Sinks.many()
									    .unicast()
									    .onBackpressureBuffer();
		Flux<Integer> flux = sink.asFlux();
		
		List<Integer> list = new ArrayList<>();
		flux.subscribe(list::add);
		
		for(int i=0; i<1000; i++) {
			int j = i;
			CompletableFuture.runAsync(() -> sink.emitNext(j, (signal, emitResult) -> Sinks.EmitResult.FAIL_NON_SERIALIZED.equals(emitResult)));
		}
		
		Util.sleepSeconds(5);
		log.info("list size: {}", list.size());
	}
}
