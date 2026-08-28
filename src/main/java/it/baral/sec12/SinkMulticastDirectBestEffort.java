package it.baral.sec12;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

/**
 * Classe dimostrativa sulla variante {@code directBestEffort()} dei sink
 * multicast, che a differenza di {@code onBackpressureBuffer()} non
 * bufferizza gli elementi per i subscriber lenti: se un subscriber non
 * riesce a tenere il passo, le emissioni verso quel subscriber vengono
 * semplicemente scartate ("best effort") invece di far fallire l'intero sink.
 */
public class SinkMulticastDirectBestEffort {

	private static final Logger log = LoggerFactory.getLogger(SinkMulticastDirectBestEffort.class);

	/**
	 * Punto di ingresso dell'applicazione. Decommentare uno dei metodi
	 * di demo per eseguire il relativo scenario.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		demoFailure();
		demoSuccess();
		Util.sleepSeconds(30);
	}

	/**
	 * Mostra come, con un sink {@code onBackpressureBuffer()} e un buffer
	 * di piccole dimensioni, un subscriber lento ("mike") causi il
	 * fallimento delle emissioni ({@code tryEmitNext} che restituisce un
	 * risultato di errore) una volta esaurito il buffer.
	 */
	private static void demoFailure() {
		System.setProperty("reactor.bufferSize.small", "16");
		Sinks.Many<Integer> sink = Sinks.many()
									   .multicast()
									   .onBackpressureBuffer();
		Flux<Integer> flux = sink.asFlux();
		
		flux.subscribe(Util.subscriber("sam"));
		flux.delayElements(Duration.ofMillis(200))
			.subscribe(Util.subscriber("mike"));
		
		for(int i=1; i<=100; i++) {
			Sinks.EmitResult result = sink.tryEmitNext(i);
			log.info("item: {} - result: {}", i, result);
		}
	}
	
	/**
	 * Mostra come {@code directBestEffort()}, combinato con
	 * {@code onBackpressureBuffer()} applicato lato subscriber lento
	 * ("mike"), permetta all'emissione di proseguire con successo anche
	 * quando un subscriber non riesce a tenere il passo.
	 */
	private static void demoSuccess() {
		System.setProperty("reactor.bufferSize.small", "16");
		Sinks.Many<Integer> sink = Sinks.many()
									    .multicast()
									    .directBestEffort();
		Flux<Integer> flux = sink.asFlux();
		
		flux.subscribe(Util.subscriber("sam"));
		flux.onBackpressureBuffer()
			.delayElements(Duration.ofMillis(200))
			.subscribe(Util.subscriber("mike"));
		
		for(int i=1; i<=100; i++) {
			Sinks.EmitResult result = sink.tryEmitNext(i);
			log.info("item: {} - result: {}", i, result);
		}
	}
}
