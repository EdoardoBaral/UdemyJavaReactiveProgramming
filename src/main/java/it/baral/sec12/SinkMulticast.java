package it.baral.sec12;

import it.baral.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Classe dimostrativa su {@code Sinks.many().multicast()}, un sink che
 * instrada gli elementi emessi a tutti i subscriber attivi al momento
 * dell'emissione, senza rimandare ai nuovi subscriber gli elementi
 * emessi prima della loro sottoscrizione.
 */
public class SinkMulticast {

	/**
	 * Punto di ingresso dell'applicazione. Decommentare uno dei metodi
	 * di demo per eseguire il relativo scenario.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		demoSinkMulticast();
		demoSinkMulticastWithWarmup();
	}

	/**
	 * Mostra come un sink multicast recapiti gli elementi solo ai subscriber
	 * già presenti al momento dell'emissione: un subscriber che si aggiunge
	 * successivamente riceve solo gli elementi emessi da quel momento in poi.
	 */
	private static void demoSinkMulticast() {
		Sinks.Many<String> sink = Sinks.many()
									   .multicast()
									   .onBackpressureBuffer();
		Flux<String> flux = sink.asFlux();
		
		flux.subscribe(Util.subscriber("sam"));
		flux.subscribe(Util.subscriber("mike"));
		
		sink.tryEmitNext("hello");
		sink.tryEmitNext("how");
		sink.tryEmitNext("are");
		sink.tryEmitNext("you");
		sink.tryEmitNext("?");
		
		Util.sleepSeconds(2);
		
		flux.subscribe(Util.subscriber("john"));
		sink.tryEmitNext("goodbye");
	}
	
	/**
	 * Mostra come gli elementi emessi da un sink multicast prima che vi sia
	 * un qualsiasi subscriber vadano perduti (nessuno riceve "hello",
	 * "how", "are", "you", "?"), a differenza di un sink unicast che li
	 * bufferizzerebbe.
	 */
	private static void demoSinkMulticastWithWarmup() {
		Sinks.Many<String> sink = Sinks.many()
									   .multicast()
									   .onBackpressureBuffer();
		Flux<String> flux = sink.asFlux();
		
		sink.tryEmitNext("hello");
		sink.tryEmitNext("how");
		sink.tryEmitNext("are");
		sink.tryEmitNext("you");
		sink.tryEmitNext("?");
		
		Util.sleepSeconds(2);
		
		flux.subscribe(Util.subscriber("sam"));
		flux.subscribe(Util.subscriber("mike"));
		flux.subscribe(Util.subscriber("john"));
		
		sink.tryEmitNext("goodbye");
	}
}
