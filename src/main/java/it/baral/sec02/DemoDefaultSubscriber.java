package it.baral.sec02;

import it.baral.common.Util;
import reactor.core.publisher.Mono;

/**
 * Classe dimostrativa della sezione 2 del corso: mostra l'uso del
 * {@link it.baral.common.DefaultSubscriber} creato tramite
 * {@link Util#subscriber()}/{@link Util#subscriber(String)} per sottoscriversi
 * piu' volte allo stesso {@link Mono}, distinguendo le sottoscrizioni nei log
 * tramite un nome.
 */
public class DemoDefaultSubscriber {

	/**
	 * Sottoscrive tre volte lo stesso {@link Mono}, una senza nome e due con
	 * nomi diversi, per mostrare come i log distinguano le sottoscrizioni.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Mono<Integer> mono = Mono.just(1);
		mono.subscribe(Util.subscriber());
		mono.subscribe(Util.subscriber("Subscriber1"));
		mono.subscribe(Util.subscriber("Subscriber2"));
	}
}
