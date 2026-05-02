package it.baral.sec03.assignment;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockPriceObserver implements Subscriber<Integer> {
	
	private static final Logger log = LoggerFactory.getLogger(StockPriceObserver.class);
	
	private Subscription subscription;
	private Integer quantity = 0;
	private Integer balance = 1000;
	
	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		this.subscription.request(Long.MAX_VALUE);
	}
	
	@Override
	public void onNext(Integer price) {
		if(price < 90 && balance >= price) {
			quantity++;
			balance = balance - price;
			log.info("Bought a stock at {} - Total quantity: {} - Remaining balance: {}", price, quantity, balance );
		} else if(price > 110 && quantity > 0) {
			log.info("Selling {} stocks at {}", quantity, price);
			balance = balance + (quantity * price);
			quantity = 0;
			subscription.cancel();
			log.info("Profit: {}", balance - 1000);
		}
	}
	
	@Override
	public void onError(Throwable t) {
		log.error("Error", t);
	}
	
	@Override
	public void onComplete() {
		log.info("Completed");
	}
}
