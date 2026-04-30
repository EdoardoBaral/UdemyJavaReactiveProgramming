package it.baral.sec02;

import it.baral.common.Util;
import it.baral.sec02.client.ExternalServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NonBlockingIO {
	
	public static final Logger log = LoggerFactory.getLogger(NonBlockingIO.class);
	
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
