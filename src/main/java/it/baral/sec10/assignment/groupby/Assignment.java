package it.baral.sec10.assignment.groupby;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Esercizio sull'operatore {@code groupBy} (sezione sec10): raggruppa un flusso continuo
 * di ordini di acquisto per categoria e applica a ciascun gruppo la relativa logica
 * di elaborazione definita in {@link OrderProcessingService}.
 */
public class Assignment {

	/**
	 * Punto di ingresso dell'esercizio: filtra gli ordini elaborabili, li raggruppa per categoria
	 * e applica a ciascun gruppo la logica di elaborazione corrispondente.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		orderStream().filter(OrderProcessingService.canProcess())
					 .groupBy(PurchaseOrder::category)
					 .flatMap(groupedFlux -> groupedFlux.transform(OrderProcessingService.getProcessor(groupedFlux.key())))
					 .subscribe(Util.subscriber());

		Util.sleepSeconds(60);
	}

	/**
	 * Genera un flusso infinito di ordini di acquisto casuali, uno ogni 200 millisecondi.
	 *
	 * @return un {@code Flux} di {@link PurchaseOrder} generati casualmente
	 */
	private static Flux<PurchaseOrder> orderStream() {
		return Flux.interval(Duration.ofMillis(200))
				   .map(i -> PurchaseOrder.create());
	}
}
