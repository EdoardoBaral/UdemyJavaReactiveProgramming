package it.baral.sec05;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class Transform {
	
	private static final Logger log = LoggerFactory.getLogger(Transform.class);
	
	record Customer(int id, String name) {}
	record PurchaseOrder(String name, int price, int quantity) {}
	
	public static void main(String[] args) {
		boolean isDebugEnabled = true;
		
		getCustomers().transform(isDebugEnabled ? addDebugger() : Function.identity())
					  .subscribe(Util.subscriber());
		
		System.out.println();
		
		isDebugEnabled = false;
		getPurchaseOrders().transform(isDebugEnabled ? addDebugger() : Function.identity())
						   .subscribe(Util.subscriber());
	}
	
	private static Flux<Customer> getCustomers() {
		return Flux.range(1, 3)
				   .map(i -> new Customer(i, Util.faker().name().fullName()));
	}
	
	private static Flux<PurchaseOrder> getPurchaseOrders() {
		return Flux.range(1, 5)
				   .map(i -> new PurchaseOrder(Util.faker().commerce().productName(),
													  Integer.parseInt(Util.faker().commerce().price().replaceAll("[^\\d]", "")),
											   		  ThreadLocalRandom.current().nextInt(1, 11)));
	}
	
	private static <T> UnaryOperator<Flux<T>> addDebugger() {
		return flux -> flux.doOnNext(i -> log.info("received: {}", i))
								   .doOnComplete(() -> log.info("completed"))
								   .doOnError(err -> log.error("error: ", err));
	}
}
