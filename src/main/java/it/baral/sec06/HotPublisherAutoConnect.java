package it.baral.sec06;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Mostra la variante hot ottenuta con {@code publish().autoConnect(0)}: a differenza
 * di {@code share()}, la sorgente inizia a emettere immediatamente, senza attendere
 * il primo sottoscrittore, perché {@code autoConnect(0)} richiede zero sottoscrittori
 * per avviare la connessione.
 */
public class HotPublisherAutoConnect {

	private static final Logger log = LoggerFactory.getLogger(HotPublisherAutoConnect.class);

	/**
	 * Sottoscrive due consumatori in momenti diversi al {@code Flux} condiviso, mostrando
	 * che l'emissione è già iniziata prima della prima sottoscrizione.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux<String> movieFlux = movieStream().publish().autoConnect(0);
		Util.sleepSeconds(2);
		movieFlux.take(4)
			     .subscribe(Util.subscriber("sub1"));
		
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
