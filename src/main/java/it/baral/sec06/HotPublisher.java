package it.baral.sec06;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Mostra il comportamento di un publisher "caldo" (hot) ottenuto con {@code share()}:
 * l'emissione inizia con il primo sottoscrittore ed è condivisa tra tutti i sottoscrittori
 * successivi, che quindi possono perdere gli elementi già emessi prima della loro iscrizione.
 */
public class HotPublisher {

	private static final Logger log = LoggerFactory.getLogger(HotPublisher.class);

	/**
	 * Sottoscrive due consumatori in momenti diversi allo stesso {@code Flux} condiviso,
	 * mostrando che il secondo sottoscrittore riceve solo gli elementi emessi da quel momento in poi.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux<String> movieFlux = movieStream().share();
		//refCount(1) è un'istruzione equivalente a share() e indica che il publisher deve avere almeno un subscriber prima di iniziare ad emettere elementi
		//Flux<String> movieFlux = movieStream().refCount(1);
		Util.sleepSeconds(2);
		movieFlux.subscribe(Util.subscriber("sub1"));
		
		Util.sleepSeconds(3);
		movieFlux.take(3)
				 .subscribe(Util.subscriber("sub2"));
		
		Util.sleepSeconds(15);
	}
	
	/**
	 * Simula lo streaming di un film: genera scene numerate con stato, una ogni secondo,
	 * fino a un massimo di 10.
	 *
	 * @return un {@code Flux} di 10 scene, emesse con cadenza di 1 secondo
	 */
	private static Flux<String> movieStream() {
		return Flux.generate(() -> {
					   log.info("request received");
					   return 1;
				   },
				   (state, sink) -> {
					   String scene = "movie scene"+ state;
					   log.info("playing scene {}", scene);
					   sink.next(scene);
					   return ++state;
				   })
				   .take(10)
				   .delayElements(Duration.ofSeconds(1))
				   .cast(String.class);
	}
}
