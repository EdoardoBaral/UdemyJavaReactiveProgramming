package it.baral.common;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.function.UnaryOperator;

/**
 * Raccolta di metodi di utilita' condivisi da tutti gli esempi del progetto:
 * creazione di {@link Subscriber} di default, generazione di dati fittizi con
 * {@link Faker}, sospensione del thread corrente e logging degli eventi del
 * ciclo di vita di un {@link Flux}.
 */
public class Util {

	private static final Logger log = LoggerFactory.getLogger(Util.class);
	private static final Faker faker = Faker.instance();

	/**
	 * Crea un {@link DefaultSubscriber} anonimo (senza nome) da usare per
	 * sottoscriversi rapidamente a un publisher negli esempi.
	 *
	 * @param <T> il tipo degli elementi emessi dal publisher
	 * @return un nuovo {@link Subscriber} che logga gli eventi ricevuti
	 */
	public static <T> Subscriber<T> subscriber() {
		return new DefaultSubscriber<>("");
	}

	/**
	 * Crea un {@link DefaultSubscriber} identificato dal nome indicato, utile
	 * per distinguere nei log piu' sottoscrizioni concorrenti.
	 *
	 * @param <T> il tipo degli elementi emessi dal publisher
	 * @param name il nome da usare per identificare il subscriber nei log
	 * @return un nuovo {@link Subscriber} che logga gli eventi ricevuti
	 */
	public static <T> Subscriber<T> subscriber(String name) {
		return new DefaultSubscriber<>(name);
	}

	/**
	 * Restituisce l'istanza condivisa di {@link Faker} usata per generare
	 * dati fittizi (nomi, testi, ecc.) negli esempi.
	 *
	 * @return l'istanza singleton di {@link Faker}
	 */
	public static Faker faker() {
		return faker;
	}

	/**
	 * Sospende il thread corrente per il numero di secondi indicato,
	 * incapsulando la {@link InterruptedException} in una {@link RuntimeException}.
	 *
	 * @param seconds il numero di secondi di attesa
	 */
	public static void sleepSeconds(int seconds) {
		try {
			Thread.sleep(Duration.ofSeconds(seconds));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sospende il thread corrente per la durata indicata, incapsulando la
	 * {@link InterruptedException} in una {@link RuntimeException}.
	 *
	 * @param duration la durata dell'attesa
	 */
	public static void sleep(Duration duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Costruisce un operatore da applicare a un {@link Flux} tramite
	 * {@code transform}/{@code transformDeferred} che logga sottoscrizione,
	 * cancellazione e completamento del flusso, identificandolo con il nome
	 * indicato.
	 *
	 * @param <T> il tipo degli elementi emessi dal flusso
	 * @param producerName il nome del produttore da usare nei messaggi di log
	 * @return un {@link UnaryOperator} che decora il {@link Flux} con il logging del ciclo di vita
	 */
	public static <T> UnaryOperator<Flux<T>> fluxLogger(String producerName) {
		return flux -> flux.doOnSubscribe(s -> log.info("subscribing to {}", producerName))
								   .doOnCancel(() -> log.info("cancelling subscription to {}", producerName))
								   .doOnComplete(() -> log.info("completed {}", producerName));
	}
}
