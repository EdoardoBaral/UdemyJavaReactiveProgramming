package it.baral.sec13;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * Classe dimostrativa sulla propagazione del Context reattivo attraverso operatori che
 * combinano più sorgenti su scheduler diversi ({@code concatWith}, {@code Flux.merge},
 * {@code subscribeOn}), per verificare che il Context scritto a valle raggiunga comunque
 * tutti i producer coinvolti, indipendentemente dal thread su cui vengono eseguiti.
 */
public class Propagation {

	private static final Logger log = LoggerFactory.getLogger(Propagation.class);

	/**
	 * Punto di ingresso dell'esempio: avvia lo scenario di propagazione del Context e
	 * attende il completamento delle elaborazioni asincrone in corso.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		contextSuccess();

		Util.sleepSeconds(5);
	}

	/**
	 * Concatena il messaggio di benvenuto con l'unione (merge) di due producer eseguiti su
	 * scheduler differenti, scrivendo la chiave {@code "user"} nel Context in modo che sia
	 * visibile a tutti gli operatori a monte.
	 */
	private static void contextSuccess() {
		getWelcomeMessage().concatWith(Flux.merge(producer1(), producer2()))
						   .contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
						   .subscribe(Util.subscriber());
	}

	/**
	 * Costruisce un messaggio di benvenuto leggendo l'utente dal Context reattivo.
	 *
	 * @return un {@link Mono} che emette il messaggio di benvenuto se la chiave {@code "user"}
	 *         è presente nel Context, oppure fallisce con {@link RuntimeException} altrimenti
	 */
	private static Mono<String> getWelcomeMessage() {
		return Mono.deferContextual(ctx -> {
			if(ctx.hasKey("user")) {
				return Mono.just("welcome %s".formatted(ctx.get("user").toString()));
			} else {
				return Mono.error(new RuntimeException("unauthorized"));
			}
		});
	}

	/**
	 * Producer di esempio, eseguito sullo scheduler {@link Schedulers#boundedElastic()}, che
	 * si limita a loggare il Context osservato al momento della sottoscrizione.
	 *
	 * @return un {@link Mono} vuoto, dopo aver loggato il Context corrente
	 */
	private static Mono<String> producer1() {
		return Mono.<String>deferContextual(ctx -> {
					    log.info("producer1 context: {}", ctx);
					    return Mono.empty();
				    })
				   .subscribeOn(Schedulers.boundedElastic());
	}

	/**
	 * Producer di esempio, eseguito sullo scheduler {@link Schedulers#parallel()}, che
	 * si limita a loggare il Context osservato al momento della sottoscrizione.
	 *
	 * @return un {@link Mono} vuoto, dopo aver loggato il Context corrente
	 */
	private static Mono<String> producer2() {
		return Mono.<String>deferContextual(ctx -> {
						log.info("producer2 context: {}", ctx);
						return Mono.empty();
					})
				   .subscribeOn(Schedulers.parallel());
	}
}
