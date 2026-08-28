package it.baral.sec03;

import it.baral.common.Util;
import it.baral.sec03.client.ExternalServiceClient;

/**
 * Classe dimostrativa della sezione 3 del corso: mostra due sottoscrizioni
 * concorrenti allo stream di nomi esposto da {@link ExternalServiceClient},
 * evidenziando come ciascuna riceva il proprio flusso indipendente di
 * eventi in modalita' non bloccante.
 */
public class NonBlockingStreamingMessages {

	/**
	 * Sottoscrive due volte lo stream di nomi del servizio esterno,
	 * attendendo poi qualche secondo affinche' i messaggi in streaming
	 * possano arrivare prima della terminazione del programma.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();
		client.getNames()
			  .subscribe(Util.subscriber("Sub1"));
		client.getNames()
			  .subscribe(Util.subscriber("Sub2"));
		Util.sleepSeconds(6);
	}
}
