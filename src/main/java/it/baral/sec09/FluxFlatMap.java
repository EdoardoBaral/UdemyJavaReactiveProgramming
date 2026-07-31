package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.applications.OrderService;
import it.baral.sec09.applications.User;
import it.baral.sec09.applications.UserService;

public class FluxFlatMap {
	
	public static void main(String[] args) {
		UserService.getUsers()
				   .map(User::id)
				   .flatMap(OrderService::getUserOrders)
				   .subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
}
