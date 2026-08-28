package it.baral.sec01.subscriber;

import lombok.Getter;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementazione manuale (senza Reactor) di {@link Subscriber} usata nella
 * sezione 1 del corso per dimostrare da zero il protocollo Reactive Streams:
 * memorizza la {@link Subscription} ricevuta e logga gli elementi, gli errori
 * e il completamento notificati dal publisher.
 */
@Getter
public class SubscriberImpl implements Subscriber<String> {

	private static final Logger log = LoggerFactory.getLogger(SubscriberImpl.class);

	private Subscription subscription;

	/**
	 * Memorizza la sottoscrizione ricevuta dal publisher, senza richiedere
	 * ancora alcun elemento (la richiesta viene fatta esplicitamente dal
	 * chiamante tramite il metodo {@code getSubscription()} generato da Lombok).
	 *
	 * @param subscription la sottoscrizione fornita dal publisher
	 */
	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
	}

	/**
	 * Logga l'indirizzo email ricevuto dal publisher.
	 *
	 * @param email l'elemento emesso dal publisher
	 */
	@Override
	public void onNext(String email) {
		log.info("Received: {}", email);
	}

	/**
	 * Logga l'errore ricevuto in caso di terminazione anomala del flusso.
	 *
	 * @param t l'eccezione che ha causato la terminazione del flusso
	 */
	@Override
	public void onError(Throwable t) {
		log.error("Error: {}", t);
	}

	/**
	 * Logga il completamento con successo del flusso.
	 */
	@Override
	public void onComplete() {
		log.info("Completed");
	}
}
