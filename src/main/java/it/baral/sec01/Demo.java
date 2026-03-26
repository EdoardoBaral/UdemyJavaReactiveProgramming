package it.baral.sec01;

import it.baral.sec01.publisher.PublisherImpl;
import it.baral.sec01.subscriber.SubscriberImpl;

import java.time.Duration;

public class Demo {
	
	/*
   		1. publisher does not produce data unless subscriber requests for it.
   		2. publisher will produce only <= subscriber requested items. publisher can also produce 0 items!
   		3. subscriber can cancel the subscription. producer should stop at that moment as subscriber is no longer interested in consuming the data
   		4. producer can send the error signal
 	*/
	public static void main(String[] args) throws Exception {
		System.out.println(">>> DEMO 1: No request from subscriber");
		demo1();
		System.out.println();
		
		System.out.println(">>> DEMO 2: Subscriber requests data from publisher");
		demo2();
		System.out.println();
		
		System.out.println(">>> DEMO 3: Subscriber cancels subscription");
		demo3();
		System.out.println();
		
		System.out.println(">>> DEMO 4: Error when publisher sends data requested by subscriber");
		demo4();
		System.out.println();
	}
	
	private static void demo1() {
		PublisherImpl publisher = new PublisherImpl();
		SubscriberImpl subscriber = new SubscriberImpl();
		publisher.subscribe(subscriber);
	}
	
	private static void demo2() throws Exception {
		PublisherImpl publisher = new PublisherImpl();
		SubscriberImpl subscriber = new SubscriberImpl();
		publisher.subscribe(subscriber);
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
	}
	
	private static void demo3() throws Exception {
		PublisherImpl publisher = new PublisherImpl();
		SubscriberImpl subscriber = new SubscriberImpl();
		publisher.subscribe(subscriber);
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().cancel();
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
	}
	
	private static void demo4() throws Exception {
		PublisherImpl publisher = new PublisherImpl();
		SubscriberImpl subscriber = new SubscriberImpl();
		publisher.subscribe(subscriber);
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().request(20);
		Thread.sleep(Duration.ofSeconds(2));
		
		subscriber.getSubscription().request(3);
		Thread.sleep(Duration.ofSeconds(2));
	}
}
