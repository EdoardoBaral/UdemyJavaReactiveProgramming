package it.baral.sec10.assignment.groupby;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class Assignment {
	
	public static void main(String[] args) {
		orderStream().filter(OrderProcessingService.canProcess())
					 .groupBy(PurchaseOrder::category)
					 .flatMap(groupedFlux -> groupedFlux.transform(OrderProcessingService.getProcessor(groupedFlux.key())))
					 .subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	private static Flux<PurchaseOrder> orderStream() {
		return Flux.interval(Duration.ofMillis(200))
				   .map(i -> PurchaseOrder.create());
	}
}
