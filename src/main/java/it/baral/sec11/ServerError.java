package it.baral.sec11;

/**
 * Eccezione non controllata che rappresenta una risposta di errore lato
 * server (qualsiasi status HTTP diverso da 200 e 400) restituita dal
 * servizio esterno simulato.
 */
public class ServerError extends RuntimeException {

	/**
	 * Costruisce l'eccezione con il messaggio fisso {@code "server error"}.
	 */
	public ServerError() {
		super("server error");
	}
}
