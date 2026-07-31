package it.baral.sec09.assignment;

import it.baral.common.Util;
import it.baral.sec09.applications.*;
import reactor.core.publisher.Mono;

import java.util.List;

public class Assignment {
	
	record UserInformation(Integer userId, String username, Integer balance, List<Order> orders) {};
	
	public static void main(String[] args) {
//		assignment1();
		assignment2();
	}
	
	private static void assignment1() {
		ExternalServiceClient client = new ExternalServiceClient();
		
		for(int i=1; i<=10; i++) {
			client.getProduct(i)
				.subscribe(Util.subscriber());
		}
		
		Util.sleepSeconds(5);
	}
	
	private static void assignment2() {
		UserService.getUsers()
			.flatMap(Assignment::getUserInformation)
			.subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
	
	private static Mono<UserInformation> getUserInformation(User user) {
		return Mono.zip(PaymentService.getUserBalance(user.id()),
						OrderService.getUserOrders(user.id()).collectList())
				   .map(t -> new UserInformation(user.id(), user.username(), t.getT1(), t.getT2()));
	}
}
