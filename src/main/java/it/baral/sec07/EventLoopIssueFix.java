package it.baral.sec07;

import it.baral.common.Util;
import it.baral.sec07.client.ExternalServiceClient;

/**
 * Dimostra la correzione del problema di blocco dell'event loop Netty: il client
 * HTTP pubblica sullo scheduler {@code boundedElastic} (vedi
 * {@link ExternalServiceClient#getProductName(int)}), cos&igrave; che l'elaborazione
 * bloccante successiva non venga eseguita sui thread dell'event loop.
 */
public class EventLoopIssueFix {

	/**
	 * Effettua 5 chiamate al servizio esterno ed elabora ciascun risultato con
	 * un'operazione bloccante, mostrando che l'event loop non viene saturato.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();

		for(int i=1; i<=5; i++) {
			client.getProductName(i)
				  .map(EventLoopIssueFix::process)
				  .subscribe(Util.subscriber());
		}
		Util.sleepSeconds(20);
	}

	/**
	 * Simula un'elaborazione bloccante (1 secondo) sul valore ricevuto.
	 *
	 * @param input il valore da elaborare
	 * @return il valore con suffisso {@code "-processed"}
	 */
	private static String process(String input) {
		Util.sleepSeconds(1);
		return input +"-processed";
	}
}
