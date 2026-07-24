package it.baral.sec05.assignment;

import it.baral.common.Util;

public class Assignment {
	
	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();
		
		//Prodotto recuperato dal servizio primario
		client.getProductName(1)
			  .subscribe(Util.subscriber());
		
		//Prodotto recuperato dal servizio di fallback in caso di risposta vuota dal primario
		client.getProductName(2)
			.subscribe(Util.subscriber());
		
		//Prodotto recuperato dal servizio di fallback in caso di timeout dal primario
		client.getProductName(3)
			.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
}
