package it.baral.sec13;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

/**
 * Classe dimostrativa sulla composizione di più chiamate a {@code contextWrite(...)}: mostra
 * come i Context scritti dagli operatori più a valle si aggiungano (append) a quelli scritti
 * più a monte, come un Context vuoto NON cancelli quelli già scritti a monte, e come una
 * chiave possa essere sovrascritta o rimossa da una scrittura successiva più a monte.
 */
public class ContextAppendUpdate {

	private static final Logger log = LoggerFactory.getLogger(ContextAppendUpdate.class);

	/**
	 * Punto di ingresso dell'esempio: esegue lo scenario di aggiornamento/rimozione di chiavi
	 * del Context (gli altri due scenari sono disabilitati via commento).
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		contextAppend();
//		contextAppendEmpty();
		contextUpdate();
	}

	/**
	 * Scenario che mostra come due Context scritti in punti diversi della catena reattiva
	 * (uno con la chiave {@code "user"}, l'altro con più chiavi) vengano uniti (append) in un
	 * unico Context visibile a monte.
	 */
	private static void contextAppend() {
		getWelcomeMessage().contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
						   .contextWrite(reactor.util.context.Context.of("a", "b").put("c", "d").put("e", "f"))
						   .subscribe(Util.subscriber());
	}

	/**
	 * Scenario che mostra come una scrittura del Context con {@link reactor.util.context.Context#empty()}
	 * non cancelli le chiavi già scritte da operatori {@code contextWrite(...)} più a monte nella catena.
	 */
	private static void contextAppendEmpty() {
		getWelcomeMessage().contextWrite(ctx -> reactor.util.context.Context.empty())
						   .contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
						   .contextWrite(reactor.util.context.Context.of("a", "b").put("c", "d").put("e", "f"))
						   .subscribe(Util.subscriber());
	}

	/**
	 * Scenario che mostra come una scrittura del Context più a monte possa sovrascrivere una
	 * chiave già presente (es. {@code "user"} passa da {@code "Paolo"} a {@code "Edoardo"}) e
	 * come una chiave possa essere rimossa con {@code ctx.delete(...)}.
	 */
	private static void contextUpdate() {
		getWelcomeMessage().contextWrite(reactor.util.context.Context.of("user", "Paolo"))
						   .contextWrite(ctx -> ctx.delete("c"))
						   .contextWrite(reactor.util.context.Context.of("user", "Edoardo"))
						   .contextWrite(reactor.util.context.Context.of("a", "b").put("c", "d").put("e", "f"))
						   .subscribe(Util.subscriber());
	}

	/**
	 * Costruisce un messaggio di benvenuto leggendo l'utente dal Context reattivo, loggando
	 * anche il contenuto completo del Context osservato in quel punto della catena.
	 *
	 * @return un {@link Mono} che emette il messaggio di benvenuto se la chiave {@code "user"}
	 *         è presente nel Context, oppure fallisce con {@link RuntimeException} altrimenti
	 */
	private static Mono<String> getWelcomeMessage() {
		return Mono.deferContextual(ctx -> {
			log.info("{}", ctx);
			if(ctx.hasKey("user")) {
				return Mono.just("welcome %s".formatted(ctx.get("user").toString()));
			} else {
				return Mono.error(new RuntimeException("unauthorized"));
			}
		});
	}
}
