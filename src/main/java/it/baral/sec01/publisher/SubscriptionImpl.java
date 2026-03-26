package it.baral.sec01.publisher;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscriptionImpl implements Subscription {
	
	private static final Logger log = LoggerFactory.getLogger(SubscriptionImpl.class);
	private static final int MAX_ITEMS = 10;
	
	private final Subscriber<? super String> subscriber;
	private final Faker faker;
	
	private boolean isCancelled = false;
	private int count = 0;
	
	public SubscriptionImpl(Subscriber<? super String> subscriber) {
		this.subscriber = subscriber;
		this.faker = Faker.instance();
	}
	
	@Override
	public void request(long requested) {
		if(!isCancelled) {
			log.info("Subscriber has requested {} items", requested);
			
			if(requested > MAX_ITEMS) {
				this.subscriber.onError(new RuntimeException("Validation failed"));
				this.isCancelled = true;
				return;
			}
			
			for(int i = 0; i < requested && count < MAX_ITEMS; i++) {
				this.subscriber.onNext(faker.internet().emailAddress());
				count++;
			}
			
			if(count == MAX_ITEMS) {
				log.info("No more data to produce");
				this.subscriber.onComplete();
				this.isCancelled = true;
			}
		}
	}
	
	@Override
	public void cancel() {
		log.info("Subscriber has cancelled");
		this.isCancelled = true;
	}
}
