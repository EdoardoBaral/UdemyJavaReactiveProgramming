package it.baral.sec06;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Mostra il comportamento di un publisher "freddo" (cold): ogni sottoscrizione a un
 * {@code Flux} creato con {@code Flux.create} innesca una nuova esecuzione indipendente
 * della sorgente, quindi ogni sottoscrittore riceve la propria sequenza di valori.
 */
public class ColdPublisher {

	private static final Logger log = LoggerFactory.getLogger(ColdPublisher.class);

	/**
	 * Sottoscrive due volte lo stesso {@code Flux} freddo, mostrando che ciascuna
	 * sottoscrizione riparte da zero il contatore condiviso.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		AtomicInteger atomicInteger = new AtomicInteger();
		Flux<Integer> flux = Flux.create(sink -> {
								 	log.info("invoked");
								 	for(int i=0; i<3; i++) {
								 		sink.next(atomicInteger.incrementAndGet());
								 	}
								 	sink.complete();
								 });
		
		flux.subscribe(Util.subscriber("sub1"));
		flux.subscribe(Util.subscriber("sub2"));
	}
}
