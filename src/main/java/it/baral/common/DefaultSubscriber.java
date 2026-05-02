package it.baral.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Getter
public class DefaultSubscriber<T> implements Subscriber<T> {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultSubscriber.class);
	
	private final String name;
	
	private Subscription subscription;
	
	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		this.subscription.request(Long.MAX_VALUE);
	}
	
	@Override
	public void onNext(T item) {
		log.info("[{}] Received: {}", this.name, item);
	}
	
	@Override
	public void onError(Throwable t) {
		log.error("[{}] Error: {}", this.name, t);
	}
	
	@Override
	public void onComplete() {
		log.info("[{}] Completed", this.name);
	}
}
