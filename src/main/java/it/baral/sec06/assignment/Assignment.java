package it.baral.sec06.assignment;

import it.baral.common.Util;

/**
 * Classe di esercitazione che collega lo stream hot di ordini di {@link ExternalServiceClient}
 * a due {@link OrderProcessor} indipendenti ({@link RevenueService} e {@link InventoryService}),
 * mostrandone lo stato aggregato in tempo reale.
 */
public class Assignment {

	/**
	 * Sottoscrive lo stream condiviso di ordini a entrambi i servizi di elaborazione e
	 * ne osserva periodicamente lo stato aggregato per 30 secondi.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
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
