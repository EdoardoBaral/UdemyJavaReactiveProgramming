package it.baral.sec11;

/**
 * Eccezione non controllata che rappresenta una risposta di errore lato
 * client (HTTP 400) restituita dal servizio esterno simulato.
 */
public class ClientError extends RuntimeException {

	/**
	 * Costruisce l'eccezione con il messaggio fisso {@code "bad request"}.
	 */
	public ClientError() {
		super("bad request");
	}
}
