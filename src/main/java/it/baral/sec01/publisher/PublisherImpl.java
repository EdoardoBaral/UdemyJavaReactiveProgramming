package it.baral.sec01.publisher;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class PublisherImpl implements Publisher<String> {
	
	@Override
	public void subscribe(Subscriber<? super String> subscriber) {
		subscriber.onSubscribe(new SubscriptionImpl(subscriber));
	}
}
