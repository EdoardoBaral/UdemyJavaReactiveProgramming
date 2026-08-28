package it.baral.sec11;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe dimostrativa sugli operatori {@code retry} e {@code retryWhen},
 * che permettono di ri-sottoscrivere una sorgente reattiva quando questa
 * termina con un errore, in modo incondizionato, con un numero massimo di
 * tentativi, con un ritardo tra i tentativi oppure filtrando il tipo di
 * errore da gestire.
 */
public class Retry {

	private static final Logger log = LoggerFactory.getLogger(Retry.class);

	/**
	 * Punto di ingresso dell'applicazione. Decommentare uno dei metodi
	 * di demo per eseguire il relativo scenario.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		demoWithoutRetry();
//		demoWithRetry();
//		demoWithRetryWhen();
		demoWithRetryWhenFilter();

		Util.sleepSeconds(10);
	}

	/**
	 * Mostra il comportamento di base senza alcun meccanismo di retry:
	 * l'errore emesso dalla sorgente termina semplicemente il flusso.
	 */
	private static void demoWithoutRetry() {
		getCountryName().subscribe(Util.subscriber());
	}

	/**
	 * Mostra {@code retry(long)}, che ri-sottoscrive la sorgente fino a un
	 * numero massimo di volte in caso di errore.
	 */
	private static void demoWithRetry() {
		getCountryName().retry(5)
						.subscribe(Util.subscriber());
	}

	/**
	 * Mostra {@code retryWhen} con una strategia a ritardo fisso, loggando
	 * un messaggio prima di ogni nuovo tentativo.
	 */
	private static void demoWithRetryWhen() {
		getCountryName().retryWhen(reactor.util.retry.Retry.fixedDelay(2, Duration.ofSeconds(1))
									                       .doBeforeRetry(i -> log.info("retrying")))
						.subscribe(Util.subscriber());
	}

	/**
	 * Mostra {@code retryWhen} con un filtro sul tipo di eccezione, così
	 * che il retry venga applicato solo agli errori che soddisfano la
	 * condizione indicata.
	 */
	private static void demoWithRetryWhenFilter() {
		getCountryName().retryWhen(reactor.util.retry.Retry.fixedDelay(2, Duration.ofSeconds(1))
														   .filter(err -> err instanceof RuntimeException))
			.subscribe(Util.subscriber());
	}

	/**
	 * Genera un {@link Mono} che fallisce con un {@link RuntimeException}
	 * per le prime due sottoscrizioni e solo dalla terza in poi emette il
	 * nome di un paese casuale, così da poter osservare l'effetto del retry.
	 *
	 * @return un {@link Mono} che simula un fallimento transitorio prima di emettere un valore
	 */
	private static Mono<String> getCountryName() {
		AtomicInteger x = new AtomicInteger(0);
		return Mono.fromSupplier(() -> {
					   if(x.incrementAndGet() < 3) {
					   		throw new RuntimeException("oops");
					   }
					   return Util.faker().country().name();
				   })
				   .doOnError(err -> log.error("ERROR: {}", err.getMessage()))
				   .doOnSubscribe(s -> log.info("subscribed"));
	}
}
