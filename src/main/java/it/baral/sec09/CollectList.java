package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.applications.OrderService;
import reactor.core.publisher.Flux;

public class CollectList {
	
	public static void main(String[] args) {
		Flux.range(1, 3)
			.flatMap(OrderService::getUserOrders)
			.collectList()
			.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
}
