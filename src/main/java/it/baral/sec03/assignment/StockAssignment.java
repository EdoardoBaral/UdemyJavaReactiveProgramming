package it.baral.sec03.assignment;

import it.baral.common.Util;
import it.baral.sec03.client.ExternalServiceClient;

/**
 * Compito della sezione 3 del corso: collega lo stream di variazioni di
 * prezzo esposto da {@link ExternalServiceClient} alla strategia di trading
 * implementata da {@link StockPriceObserver}.
 */
public class StockAssignment {

	/**
	 * Sottoscrive lo stream di variazioni di prezzo con
	 * {@link StockPriceObserver}, attendendo poi 25 secondi affinche' la
	 * strategia di trading possa reagire agli eventi in arrivo.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();
		client.getPriceChanges()
			  .subscribe(new StockPriceObserver());
		
		Util.sleepSeconds(25);
	}
}
