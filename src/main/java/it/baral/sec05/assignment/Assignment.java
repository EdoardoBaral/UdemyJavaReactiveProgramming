package it.baral.sec05.assignment;

import it.baral.common.Util;

/**
 * Classe di esercitazione che usa {@link ExternalServiceClient} per mostrare i tre
 * scenari di recupero del nome prodotto: risposta primaria diretta, fallback per
 * risposta vuota e fallback per timeout.
 */
public class Assignment {

	/**
	 * Richiama {@link ExternalServiceClient#getProductName(int)} per tre prodotti
	 * che innescano rispettivamente il percorso primario, il fallback per risposta
	 * vuota e il fallback per timeout.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
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
