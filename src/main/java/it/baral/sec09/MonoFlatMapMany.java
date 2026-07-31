package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.applications.OrderService;
import it.baral.sec09.applications.UserService;

public class MonoFlatMapMany {
	
	public static void main(String[] args) {
		UserService.getUserId("sam")
				   .flatMapMany(OrderService::getUserOrders)
				   .subscribe(Util.subscriber());
		
		Util.sleepSeconds(2);
	}
}
