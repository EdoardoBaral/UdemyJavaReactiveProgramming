package it.baral.sec11;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.util.retry.Retry;

import java.time.Duration;

/**
 * Classe dimostrativa che applica gli operatori {@code repeat} e
 * {@code retry} a chiamate reali verso {@link ExternalServiceClient},
 * mostrando come gestire rispettivamente la ripetizione di una chiamata
 * riuscita e il nuovo tentativo dopo un errore server.
 */
public class ExternalServiceDemo {

	private static final Logger log = LoggerFactory.getLogger(ExternalServiceDemo.class);

	/**
	 * Punto di ingresso dell'applicazione. Decommentare uno dei metodi
	 * di demo per eseguire il relativo scenario.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		repeat();
		retry();

		Util.sleepSeconds(60);
	}

	/**
	 * Invoca ripetutamente {@code getCountry()} finché il servizio non
	 * restituisce "Canada", usando {@code repeat} e {@code takeUntil}.
	 */
	private static void repeat() {
		ExternalServiceClient client = new ExternalServiceClient();
		client.getCountry()
			  .repeat()
			  .takeUntil("Canada"::equalsIgnoreCase)
			  .subscribe(Util.subscriber());
	}

	/**
	 * Invoca {@code getProductName} applicando la strategia di retry
	 * definita da {@link #retryOnServerError()}, così da ritentare
	 * automaticamente in caso di errore server transitorio.
	 */
	private static void retry() {
		ExternalServiceClient client = new ExternalServiceClient();
		client.getProductName(2) //1 -> Bad Request, 2 -> esito casuale
			  .retryWhen(retryOnServerError())
			  .subscribe(Util.subscriber());
	}

	/**
	 * Costruisce una strategia di retry a ritardo fisso (1 secondo, fino a
	 * 20 tentativi) applicata solo agli errori di tipo {@link ServerError},
	 * loggando un messaggio prima di ogni nuovo tentativo.
	 *
	 * @return la strategia di {@link Retry} da applicare con {@code retryWhen}
	 */
	public static Retry retryOnServerError() {
		return Retry.fixedDelay(20, Duration.ofSeconds(1))
				    .filter(ex -> ex instanceof ServerError)
				    .doBeforeRetry(rs -> log.info("retrying..."));
	}
}
