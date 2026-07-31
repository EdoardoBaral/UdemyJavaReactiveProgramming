package it.baral.common;

import com.github.javafaker.Faker;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.function.UnaryOperator;

public class Util {
	
	private static final Logger log = LoggerFactory.getLogger(Util.class);
	private static final Faker faker = Faker.instance();
	
	public static <T> Subscriber<T> subscriber() {
		return new DefaultSubscriber<>("");
	}
	
	public static <T> Subscriber<T> subscriber(String name) {
		return new DefaultSubscriber<>(name);
	}
	
	public static Faker faker() {
		return faker;
	}
	
	public static void sleepSeconds(int seconds) {
		try {
			Thread.sleep(Duration.ofSeconds(seconds));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void sleep(Duration duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static <T> UnaryOperator<Flux<T>> fluxLogger(String producerName) {
		return flux -> flux.doOnSubscribe(s -> log.info("subscribing to {}", producerName))
								   .doOnCancel(() -> log.info("cancelling subscription to {}", producerName))
								   .doOnComplete(() -> log.info("completed {}", producerName));
	}
}
