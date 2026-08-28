package it.baral.sec13.client;

import reactor.util.context.Context;

import java.util.Map;
import java.util.function.Function;

/**
 * Servizio di supporto che associa gli utenti conosciuti alla propria categoria di
 * appartenenza, usata poi dal {@link it.baral.sec13.RateLimiter} per determinare
 * i tentativi disponibili.
 */
public class UserService {

	private static final Map<String, String> USER_CATEGORY = Map.of("sam", "standard",
																    "mike", "prime");

	/**
	 * Costruisce una funzione di arricchimento del Context reattivo che, a partire dalla
	 * chiave {@code "user"} già presente, aggiunge la chiave {@code "category"} con la
	 * categoria dell'utente (se riconosciuto in {@link #USER_CATEGORY}).
	 *
	 * @return una funzione da usare con {@code contextWrite(...)}: restituisce il Context
	 *         arricchito con la categoria se l'utente è noto, altrimenti un Context vuoto
	 *         (che, unito al Context esistente, non aggiunge la chiave {@code "category"})
	 */
	static Function<Context, Context> userCategoryContext() {
		return ctx -> ctx.<String>getOrEmpty("user")
						  		 .filter(USER_CATEGORY::containsKey)
						  		 .map(USER_CATEGORY::get)
						  		 .map(category -> ctx.put("category", category))
						  		 .orElse(Context.empty());
	}
}
