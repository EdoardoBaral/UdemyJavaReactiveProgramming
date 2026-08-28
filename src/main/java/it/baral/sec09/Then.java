package it.baral.sec09;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * Dimostra l'operatore {@code then}: ignora gli elementi emessi da una sorgente e, al suo
 * completamento, passa l'esecuzione a un secondo {@link Mono}.
 */
public class Then {

	private static final Logger log = LoggerFactory.getLogger(Then.class);

	/**
	 * Punto di ingresso della demo: salva una lista di record e, solo al termine,
	 * invia una notifica tramite {@code then}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		List<String> records = List.of("a", "b", "c");

		saveRecords(records).then(sendNotification(records))
							.subscribe(Util.subscriber());

		Util.sleepSeconds(5);
	}

	/**
	 * Simula il salvataggio di una lista di record, emettendo un elemento per ciascuno
	 * con un ritardo artificiale.
	 *
	 * @param records lista dei record da salvare
	 * @return un {@code Flux} che emette la conferma di salvataggio di ogni record
	 */
	private static Flux<String> saveRecords(List<String> records) {
		return Flux.fromIterable(records)
				   .map(r -> "saved "+ r)
				   .delayElements(Duration.ofMillis(500));
	}

	/**
	 * Simula l'invio di una notifica relativa ai record salvati.
	 *
	 * @param records lista dei record salvati con successo
	 * @return un {@code Mono<Void>} che completa una volta effettuata la notifica
	 */
	private static Mono<Void> sendNotification(List<String> records) {
		return Mono.fromRunnable(() -> log.info("All these records saved successfully: {}", records));
	}
}
