package it.baral.sec04;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 * Introduce {@code Flux.generate}, un operatore che genera valori in modo sincrono e
 * pull-based (un elemento per ogni richiesta a valle) tramite un {@link reactor.core.publisher.SynchronousSink}.
 */
public class FluxGenerate {

	private static final Logger log = LoggerFactory.getLogger(FluxGenerate.class);

	/**
	 * Esegue la demo relativa a {@link #generateSingleItem()}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		//generateEndlessLoop();
		//generateLimitedItems();
		generateSingleItem();
	}

	/**
	 * Mostra che, senza mai invocare {@code complete()}, {@code Flux.generate} continua
	 * a essere richiamato indefinitamente a ogni richiesta del sottoscrittore.
	 */
	private static void generateEndlessLoop() {
		Flux.generate(synchronousSink -> {
				log.info("invoked");
				synchronousSink.next(1);
			})
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Mostra come limitare un {@code Flux.generate} senza condizione di completamento
	 * propria, applicando l'operatore {@code take(4)} a valle.
	 */
	private static void generateLimitedItems() {
		Flux.generate(synchronousSink -> {
				log.info("invoked");
				synchronousSink.next(1);
			})
			.take(4)
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Mostra un generatore che emette un solo elemento e poi invoca subito
	 * {@code complete()} sul {@link reactor.core.publisher.SynchronousSink}.
	 */
	private static void generateSingleItem() {
		Flux.generate(synchronousSink -> {
				log.info("invoked");
				synchronousSink.next(1);
				synchronousSink.complete();
			})
			.subscribe(Util.subscriber());
	}
}
