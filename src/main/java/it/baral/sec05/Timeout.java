package it.baral.sec05;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Mostra l'operatore {@code timeout} in diversi scenari: nessun timeout, timeout con
 * errore, timeout con {@code Mono} di fallback e timeout multipli applicati in cascata.
 */
public class Timeout {

	private static final Logger log = LoggerFactory.getLogger(Timeout.class);

	/**
	 * Esegue la demo relativa a {@link #multipleTimeoutsCase()}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		noTimeoutCase();
//		timeoutCase();
//		timeoutWithFallbackCase();
		multipleTimeoutsCase();
	}

	/**
	 * Mostra un timeout di 10 secondi su un {@code Mono} che risponde in 3 secondi:
	 * il timeout non scatta e viene emesso il valore reale.
	 */
	private static void noTimeoutCase() {
		getProduct().timeout(Duration.ofSeconds(10))
					.onErrorReturn("fallback")
					.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
	
	/**
	 * Mostra un timeout di 1 secondo su un {@code Mono} che risponde in 3 secondi:
	 * il timeout scatta e l'errore viene sostituito da {@code onErrorReturn}.
	 */
	private static void timeoutCase() {
		getProduct().timeout(Duration.ofSeconds(1))
					.onErrorReturn("fallback")
					.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
	
	/**
	 * Mostra un timeout di 2 secondi con un {@code Mono} di fallback fornito
	 * direttamente all'operatore {@code timeout}.
	 */
	private static void timeoutWithFallbackCase() {
		getProduct().timeout(Duration.ofSeconds(2), getProductFallback())
					.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
	
	/**
	 * Mostra due timeout applicati in cascata: un primo timeout con fallback sul
	 * {@code Mono} primario, e un secondo timeout più stretto applicato al risultato,
	 * che finisce per scattare per primo.
	 */
	private static void multipleTimeoutsCase() {
		Mono<String> mono = getProduct().timeout(Duration.ofSeconds(1), getProductFallback());
		
		mono.timeout(Duration.ofMillis(200))
			.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
	
	/**
	 * Simula il servizio primario: emette il nome di un prodotto dopo un ritardo di 3 secondi.
	 *
	 * @return un {@code Mono} che emette il nome del prodotto dopo 3 secondi
	 */
	private static Mono<String> getProduct() {
		return Mono.just("Primary service - "+ Util.faker().commerce().productName())
				   .delayElement(Duration.ofSeconds(3));
	}
	
	/**
	 * Simula il servizio di fallback: emette il nome di un prodotto dopo un ritardo di 2 secondi.
	 *
	 * @return un {@code Mono} che emette il nome del prodotto dopo 2 secondi
	 */
	private static Mono<String> getProductFallback() {
		return Mono.just("Fallback service - " + Util.faker().commerce().productName())
				   .delayElement(Duration.ofSeconds(2));
	}
}
