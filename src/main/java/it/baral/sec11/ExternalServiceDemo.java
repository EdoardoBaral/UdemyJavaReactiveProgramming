package it.baral.sec11;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.util.retry.Retry;

import java.time.Duration;

public class ExternalServiceDemo {
	
	private static final Logger log = LoggerFactory.getLogger(ExternalServiceDemo.class);
	
	public static void main(String[] args) {
//		repeat();
		retry();
		
		Util.sleepSeconds(60);
	}
	
	private static void repeat() {
		ExternalServiceClient client = new ExternalServiceClient();
		client.getCountry()
			  .repeat()
			  .takeUntil("Canada"::equalsIgnoreCase)
			  .subscribe(Util.subscriber());
	}
	
	private static void retry() {
		ExternalServiceClient client = new ExternalServiceClient();
		client.getProductName(2) //1 -> Bad Request, 2 -> esito casuale
			  .retryWhen(retryOnServerError())
			  .subscribe(Util.subscriber());
	}
	
	public static Retry retryOnServerError() {
		return Retry.fixedDelay(20, Duration.ofSeconds(1))
				    .filter(ex -> ex instanceof ServerError)
				    .doBeforeRetry(rs -> log.info("retrying..."));
	}
}
