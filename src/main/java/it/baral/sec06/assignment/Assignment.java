package it.baral.sec06.assignment;

import it.baral.common.Util;

public class Assignment {
	
	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();
		OrderProcessor revenueService = new RevenueService();
		OrderProcessor inventoryService = new InventoryService();
		
		client.orderStream().subscribe(revenueService::consume);
		client.orderStream().subscribe(inventoryService::consume);
		
		inventoryService.stream()
						.subscribe(Util.subscriber("inventory"));
		
		revenueService.stream()
					  .subscribe(Util.subscriber("revenue"));
		
		Util.sleepSeconds(30);
	}
}
