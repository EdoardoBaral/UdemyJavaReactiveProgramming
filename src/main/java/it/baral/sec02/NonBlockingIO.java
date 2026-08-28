package it.baral.sec02;

import it.baral.common.Util;
import it.baral.sec02.client.ExternalServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe dimostrativa della sezione 2 del corso: confronta l'invocazione
 * bloccante (tramite {@code block()}, commentata nel codice) con quella non
 * bloccante (tramite {@code subscribe()}) di 50 chiamate HTTP verso
 * {@link ExternalServiceClient}, mostrando come la versione non bloccante
 * invii le richieste quasi simultaneamente senza attendere le risposte.
 */
public class NonBlockingIO {

	public static final Logger log = LoggerFactory.getLogger(NonBlockingIO.class);

	/**
	 * Effettua 50 chiamate al servizio esterno in modo non bloccante,
	 * attendendo poi qualche secondo affinche' le risposte asincrone possano
	 * arrivare prima della terminazione del programma.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();
		log.info("Starting");
		
		//Versione bloccante a solo scopo dimostrativo: le richieste vengono processate sequenzialmente, una per volta
//		for(int i=1; i<=50; i++) {
//			String name = client.getProductName(i)
//								.block();
//			log.info(name);
//		}
		
		//Versione non bloccante: le richieste vengono inviate quasi simultaneamente senza attendere la risposta prima di inviare la successiva
		for(int i=1; i<=50; i++) {
			client.getProductName(i)
				  .subscribe(Util.subscriber());
		}
		Util.sleepSeconds(2);
	}
}
