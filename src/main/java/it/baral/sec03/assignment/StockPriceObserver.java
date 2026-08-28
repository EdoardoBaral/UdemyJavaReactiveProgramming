package it.baral.sec03.assignment;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Compito della sezione 3 del corso: {@link Subscriber} che simula una
 * strategia di trading su un titolo azionario, acquistando quando il prezzo
 * scende sotto 90 (finche' il saldo lo permette) e vendendo l'intera
 * posizione quando il prezzo sale sopra 110, annullando poi la sottoscrizione
 * dopo la vendita.
 */
public class StockPriceObserver implements Subscriber<Integer> {

	private static final Logger log = LoggerFactory.getLogger(StockPriceObserver.class);

	private Subscription subscription;
	private Integer quantity = 0;
	private Integer balance = 1000;

	/**
	 * Memorizza la sottoscrizione ricevuta dal publisher e richiede
	 * immediatamente il numero massimo di elementi (nessuna backpressure).
	 *
	 * @param subscription la sottoscrizione fornita dal publisher
	 */
	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		this.subscription.request(Long.MAX_VALUE);
	}

	/**
	 * Applica la strategia di trading al nuovo prezzo ricevuto: acquista se
	 * il prezzo e' basso e il saldo e' sufficiente, oppure vende l'intera
	 * posizione (annullando la sottoscrizione) se il prezzo e' alto e sono
	 * presenti titoli in portafoglio.
	 *
	 * @param price il nuovo prezzo del titolo ricevuto dal publisher
	 */
	@Override
	public void onNext(Integer price) {
		if(price < 90 && balance >= price) {
			quantity++;
			balance = balance - price;
			log.info("Bought a stock at {} - Total quantity: {} - Remaining balance: {}", price, quantity, balance );
		} else if(price > 110 && quantity > 0) {
			log.info("Selling {} stocks at {}", quantity, price);
			balance = balance + (quantity * price);
			quantity = 0;
			subscription.cancel();
			log.info("Profit: {}", balance - 1000);
		}
	}

	/**
	 * Logga l'errore ricevuto in caso di terminazione anomala del flusso.
	 *
	 * @param t l'eccezione che ha causato la terminazione del flusso
	 */
	@Override
	public void onError(Throwable t) {
		log.error("Error", t);
	}

	/**
	 * Logga il completamento con successo del flusso.
	 */
	@Override
	public void onComplete() {
		log.info("Completed");
	}
}
