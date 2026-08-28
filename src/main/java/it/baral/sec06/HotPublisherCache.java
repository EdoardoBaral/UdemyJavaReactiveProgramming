package it.baral.sec06;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Mostra un publisher hot combinato con {@code replay(10)}: gli ultimi 10 elementi
 * emessi vengono mantenuti in cache e ritrasmessi a ogni nuovo sottoscrittore,
 * anche se si iscrive dopo che tali elementi sono già stati emessi.
 */
public class HotPublisherCache {

	private static final Logger log = LoggerFactory.getLogger(HotPublisherCache.class);

	/**
	 * Sottoscrive due consumatori in momenti diversi al {@code Flux} con replay,
	 * mostrando che entrambi ricevono gli elementi già emessi grazie alla cache.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux<Integer> stockFlux = stockStream().replay(10).autoConnect(0);
		Util.sleepSeconds(4);
		log.info("sub1 is joining");
		stockFlux.subscribe(Util.subscriber("sub1"));
		
		Util.sleepSeconds(4);
		log.info("sub2 is joining");
		stockFlux.subscribe(Util.subscriber("sub2"));
		
		Util.sleepSeconds(15);
	}
	
	/**
	 * Simula lo streaming di un prezzo azionario: genera un nuovo prezzo casuale ogni 3 secondi.
	 *
	 * @return un {@code Flux} infinito di prezzi casuali, emessi con cadenza di 3 secondi
	 */
	private static Flux<Integer> stockStream() {
		return Flux.generate(sink -> sink.next(Util.faker().random().nextInt(1, 100)))
				   .delayElements(Duration.ofSeconds(3))
				   .doOnNext(price -> log.info("emitting price: {}", price))
				   .cast(Integer.class);
	}
}
