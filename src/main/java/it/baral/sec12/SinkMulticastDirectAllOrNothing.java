package it.baral.sec12;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

/**
 * Classe dimostrativa sulla variante {@code directAllOrNothing()} dei sink
 * multicast: se anche un solo subscriber non riesce a ricevere un elemento,
 * l'emissione fallisce per tutti i subscriber ("tutto o niente"), a
 * differenza della semantica "best effort" vista in
 * {@link SinkMulticastDirectBestEffort}.
 */
public class SinkMulticastDirectAllOrNothing {

	private static final Logger log = LoggerFactory.getLogger(SinkMulticastDirectAllOrNothing.class);

	/**
	 * Punto di ingresso dell'applicazione.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		demoSuccess();
		Util.sleepSeconds(30);
	}

	/**
	 * Mostra come, con un sink {@code directAllOrNothing()} e un subscriber
	 * lento ("mike") a fianco di uno reattivo ("sam"), le emissioni
	 * falliscano non appena il subscriber lento non riesce a ricevere
	 * l'elemento, interrompendo la consegna per entrambi i subscriber.
	 */
	private static void demoSuccess() {
		System.setProperty("reactor.bufferSize.small", "16");
		Sinks.Many<Integer> sink = Sinks.many()
									    .multicast()
									    .directAllOrNothing();
		Flux<Integer> flux = sink.asFlux();
		
		flux.subscribe(Util.subscriber("sam"));
		flux.delayElements(Duration.ofMillis(200))
			.subscribe(Util.subscriber("mike"));
		
		for(int i=1; i<=100; i++) {
			Sinks.EmitResult result = sink.tryEmitNext(i);
			log.info("item: {} - result: {}", i, result);
		}
	}
}
