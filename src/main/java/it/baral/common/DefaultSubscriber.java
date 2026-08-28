package it.baral.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Subscriber} generico riutilizzabile in tutti gli esempi del progetto:
 * al momento della sottoscrizione richiede subito la quantita' massima di
 * elementi ({@code Long.MAX_VALUE}, cioe' nessun controllo di backpressure) e
 * si limita a loggare gli eventi ricevuti (elemento, errore, completamento),
 * identificandoli con un nome descrittivo.
 *
 * @param <T> il tipo degli elementi emessi dal {@link org.reactivestreams.Publisher} sottoscritto
 */
@RequiredArgsConstructor
@Getter
public class DefaultSubscriber<T> implements Subscriber<T> {

	private static final Logger log = LoggerFactory.getLogger(DefaultSubscriber.class);

	private final String name;

	private Subscription subscription;

	/**
	 * Riceve la {@link Subscription} dal publisher e richiede immediatamente
	 * il numero massimo di elementi, disabilitando di fatto la backpressure.
	 *
	 * @param subscription la sottoscrizione fornita dal publisher
	 */
	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		this.subscription.request(Long.MAX_VALUE);
	}

	/**
	 * Logga ogni elemento ricevuto dal publisher.
	 *
	 * @param item l'elemento emesso dal publisher
	 */
	@Override
	public void onNext(T item) {
		log.info("[{}] Received: {}", this.name, item);
	}

	/**
	 * Logga l'errore ricevuto in caso di terminazione anomala del flusso.
	 *
	 * @param t l'eccezione che ha causato la terminazione del flusso
	 */
	@Override
	public void onError(Throwable t) {
		log.error("[{}] Error: {}", this.name, t);
	}

	/**
	 * Logga il completamento con successo del flusso.
	 */
	@Override
	public void onComplete() {
		log.info("[{}] Completed", this.name);
	}
}
