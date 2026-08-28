package it.baral.sec02;

import it.baral.sec01.subscriber.SubscriberImpl;
import reactor.core.publisher.Mono;

/**
 * Classe dimostrativa della sezione 2 del corso: mostra il comportamento di
 * {@link Mono#just(Object)}, che emette immediatamente il singolo valore
 * fornito e completa, ignorando eventuali richieste multiple o la
 * cancellazione successiva alla sottoscrizione.
 */
public class MonoJust {

	/**
	 * Sottoscrive un {@link SubscriberImpl} a un {@link Mono#just(Object)} ed
	 * effettua richieste e cancellazione successive per osservarne l'effetto
	 * (nullo, dato che il valore e' gia' stato emesso alla sottoscrizione).
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Mono<String> mono = Mono.just("Edoardo");
		SubscriberImpl subscriber = new SubscriberImpl();
		mono.subscribe(subscriber);
		
		subscriber.getSubscription().request(3);
		subscriber.getSubscription().request(3);
		subscriber.getSubscription().cancel();
	}
}
