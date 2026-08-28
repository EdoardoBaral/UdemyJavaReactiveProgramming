package it.baral.sec12;

import it.baral.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Classe dimostrativa su {@code Sinks.many().replay()}, un sink multicast
 * che memorizza gli elementi emessi e li ritrasmette ("replay") a ogni
 * nuovo subscriber, così che questo riceva anche gli elementi emessi
 * prima della sua sottoscrizione.
 */
public class SinkMulticastReplay {

	/**
	 * Punto di ingresso dell'applicazione.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		demoSinkMulticastReplay();
		Util.sleepSeconds(10);
	}

	/**
	 * Mostra come un subscriber ("john") che si aggiunge dopo l'emissione
	 * dei primi elementi riceva comunque, grazie al replay, tutti gli
	 * elementi già emessi in precedenza oltre a quelli successivi.
	 */
	private static void demoSinkMulticastReplay() {
		Sinks.Many<String> sink = Sinks.many()
									   .replay()
									   .all();
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
}
