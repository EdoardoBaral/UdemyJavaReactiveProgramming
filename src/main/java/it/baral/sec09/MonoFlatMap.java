package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.applications.UserService;
import reactor.core.publisher.Mono;

public class MonoFlatMap {
	
	public static void main(String[] args) {
		UserService.getUserId("sam")
				   .flatMap(userId -> Mono.fromSupplier(() -> "Hello "+ userId))
				   .subscribe(Util.subscriber());
	}
}
