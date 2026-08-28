package it.baral.sec01.publisher;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

/**
 * Implementazione manuale (senza Reactor) di {@link Publisher} usata nella
 * sezione 1 del corso per dimostrare da zero il protocollo Reactive Streams:
 * ad ogni sottoscrizione crea una nuova {@link SubscriptionImpl} dedicata al
 * subscriber, delegandole la produzione effettiva dei dati.
 */
public class PublisherImpl implements Publisher<String> {

	/**
	 * Notifica al subscriber una nuova {@link SubscriptionImpl}, tramite la
	 * quale potra' richiedere gli elementi.
	 *
	 * @param subscriber il subscriber che si sta sottoscrivendo al publisher
	 */
	@Override
	public void subscribe(Subscriber<? super String> subscriber) {
		subscriber.onSubscribe(new SubscriptionImpl(subscriber));
	}
}
