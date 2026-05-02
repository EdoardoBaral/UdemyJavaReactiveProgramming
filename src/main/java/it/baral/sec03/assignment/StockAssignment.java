package it.baral.sec03.assignment;

import it.baral.common.Util;
import it.baral.sec03.client.ExternalServiceClient;

public class StockAssignment {
	
	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();
		client.getPriceChanges()
			  .subscribe(new StockPriceObserver());
		
		Util.sleepSeconds(25);
	}
}
