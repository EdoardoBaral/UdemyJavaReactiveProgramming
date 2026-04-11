package it.baral.sec02;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public class MonoFromRunnable {
	
	private static final Logger log = LoggerFactory.getLogger(MonoFromRunnable.class);
	
	public static void main(String[] args) {
		getProduceName(2).subscribe(Util.subscriber());
	}
	
	private static Mono<String> getProduceName(int productId) {
		if(productId == 1) {
			return Mono.fromSupplier(() -> Util.faker().commerce().productName());
		}
		return Mono.fromRunnable(() -> notifyBusiness(productId));
	}
	
	private static void notifyBusiness(int productId) {
		log.info("Notifying business product is not available: {}", productId);
	}
}
