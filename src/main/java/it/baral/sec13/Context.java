package it.baral.sec13;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Classe dimostrativa sull'uso base del Context reattivo di Project Reactor: mostra come
 * un valore scritto con {@code contextWrite(...)} venga letto a monte da un operatore
 * {@code deferContextual}, e cosa accade quando la chiave attesa non è presente.
 */
public class Context {

	private static final Logger log = LoggerFactory.getLogger(Context.class);

	/**
	 * Punto di ingresso dell'esempio: esegue lo scenario in cui il Context contiene
	 * la chiave attesa (lo scenario di fallimento è disabilitato via commento).
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		contextFailure();
		contextSuccess();
	}

	/**
	 * Scenario in cui il Context viene scritto con una chiave ({@code "a"}) diversa da quella
	 * attesa da {@link #getWelcomeMessage()} ({@code "user"}), causando il fallimento del Mono.
	 */
	private static void contextFailure() {
		getWelcomeMessage().contextWrite(reactor.util.context.Context.of("a", "b"))
						   .subscribe(Util.subscriber());
	}

	/**
	 * Scenario in cui il Context viene scritto con la chiave {@code "user"} attesa,
	 * permettendo a {@link #getWelcomeMessage()} di completare con successo.
	 */
	private static void contextSuccess() {
		getWelcomeMessage().contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
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
}
