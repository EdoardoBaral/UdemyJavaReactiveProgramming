package it.baral.sec12;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Classe dimostrativa su {@code Sinks.many().unicast()}, un sink che
 * bufferizza gli elementi emessi e li instrada a un solo subscriber:
 * un secondo tentativo di sottoscrizione viene rifiutato con errore.
 */
public class SinkUnicast {

	private static final Logger log = LoggerFactory.getLogger(SinkUnicast.class);

	/**
	 * Punto di ingresso dell'applicazione. Decommentare uno dei metodi
	 * di demo per eseguire il relativo scenario.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		demoSinkUnicast();
		demoSinkUnicastMultipleSubscribers();
	}

	/**
	 * Crea un sink unicast, emette alcuni elementi prima ancora che vi sia
	 * un subscriber, e mostra come questi vengano bufferizzati e recapitati
	 * al momento della sottoscrizione.
	 */
	private static void demoSinkUnicast() {
		Sinks.Many<String> sink = Sinks.many()
									   .unicast()
									   .onBackpressureBuffer();
		Flux<String> flux = sink.asFlux();
		
		sink.tryEmitNext("hello");
		sink.tryEmitNext("how");
		sink.tryEmitNext("are");
		sink.tryEmitNext("you");
		sink.tryEmitNext("?");
		
		flux.subscribe(Util.subscriber());
	}
	
	/**
	 * Mostra il comportamento di un sink unicast quando si tenta di
	 * sottoscrivervi più di un subscriber: solo il primo riceve gli
	 * elementi, mentre il secondo riceve un errore.
	 */
	private static void demoSinkUnicastMultipleSubscribers() {
		Sinks.Many<String> sink = Sinks.many()
									   .unicast()
									   .onBackpressureBuffer();
		Flux<String> flux = sink.asFlux();
		
		sink.tryEmitNext("hello");
		sink.tryEmitNext("how");
		sink.tryEmitNext("are");
		sink.tryEmitNext("you");
		sink.tryEmitNext("?");
		
		flux.subscribe(Util.subscriber("sam"));
		flux.subscribe(Util.subscriber("mike"));
	}
}
