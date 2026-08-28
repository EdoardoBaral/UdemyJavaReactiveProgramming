package it.baral.sec01.publisher;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementazione manuale (senza Reactor) di {@link Subscription} usata nella
 * sezione 1 del corso per dimostrare da zero il protocollo Reactive Streams e
 * il meccanismo di backpressure: produce al massimo {@value #MAX_ITEMS}
 * indirizzi email fittizi, emettendo solo la quantita' richiesta dal
 * subscriber ad ogni chiamata a {@link #request(long)} e fallendo se la
 * richiesta supera il limite massimo.
 */
public class SubscriptionImpl implements Subscription {

	private static final Logger log = LoggerFactory.getLogger(SubscriptionImpl.class);
	private static final int MAX_ITEMS = 10;

	private final Subscriber<? super String> subscriber;
	private final Faker faker;

	private boolean isCancelled = false;
	private int count = 0;

	/**
	 * Crea la sottoscrizione associata al subscriber indicato, inizializzando
	 * il generatore di dati fittizi.
	 *
	 * @param subscriber il subscriber a cui inviare gli elementi prodotti
	 */
	public SubscriptionImpl(Subscriber<? super String> subscriber) {
		this.subscriber = subscriber;
		this.faker = Faker.instance();
	}

	/**
	 * Produce fino a {@code requested} indirizzi email fittizi (senza mai
	 * superare {@value #MAX_ITEMS} elementi totali). Se la richiesta supera
	 * il limite massimo consentito, notifica un errore al subscriber e
	 * termina la sottoscrizione; se il numero massimo di elementi e' stato
	 * raggiunto, notifica il completamento.
	 *
	 * @param requested il numero di elementi richiesti dal subscriber
	 */
	@Override
	public void request(long requested) {
		if(!isCancelled) {
			log.info("Subscriber has requested {} items", requested);

			if(requested > MAX_ITEMS) {
				this.subscriber.onError(new RuntimeException("Validation failed"));
				this.isCancelled = true;
				return;
			}

			for(int i = 0; i < requested && count < MAX_ITEMS; i++) {
				this.subscriber.onNext(faker.internet().emailAddress());
				count++;
			}

			if(count == MAX_ITEMS) {
				log.info("No more data to produce");
				this.subscriber.onComplete();
				this.isCancelled = true;
			}
		}
	}

	/**
	 * Segnala l'annullamento della sottoscrizione da parte del subscriber,
	 * impedendo ulteriori produzioni di dati.
	 */
	@Override
	public void cancel() {
		log.info("Subscriber has cancelled");
		this.isCancelled = true;
	}
}
