package it.baral.sec08;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

public class BackPressureStrategies {
	
	private static final Logger log = LoggerFactory.getLogger(BackPressureStrategies.class);
	
	public static void main(String[] args) {
//		bufferStrategy();
//		errorStrategy();
//		bufferSizeStrategy();
//		dropStrategy();
//		lastStrategy();
		overflowStrategy();
	}
	
	private static int timeConsumingTask(int i) {
		log.info("received: {}", i);
		Util.sleepSeconds(1);
		return i;
	}
	
	private static void bufferStrategy() {
		Flux<Integer> producer = Flux.create((sink) -> {
									 		 	for(int i=1; i<=500 && !sink.isCancelled(); i++) {
									 		 		log.info("generating: {}", i);
									 		 		Util.sleep(Duration.ofMillis(50));
									 		 		sink.next(i);
									 		 	}
									 		 	sink.complete();
									 		 })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.onBackpressureBuffer()
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	private static void errorStrategy() {
		Flux<Integer> producer = Flux.create((sink) -> {
									 		 	for(int i=1; i<=500 && !sink.isCancelled(); i++) {
									 		 		log.info("generating: {}", i);
									 		 		Util.sleep(Duration.ofMillis(50));
									 		 		sink.next(i);
									 		 	}
									 		 	sink.complete();
									 		 })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.onBackpressureError()
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	private static void bufferSizeStrategy() {
		Flux<Integer> producer = Flux.create((sink) -> {
									 		 	for(int i=1; i<=500 && !sink.isCancelled(); i++) {
									 		 		log.info("generating: {}", i);
									 		 		Util.sleep(Duration.ofMillis(50));
									 		 		sink.next(i);
									 		 	}
									 		 	sink.complete();
									 		 })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.onBackpressureBuffer(10)
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	private static void dropStrategy() {
		Flux<Integer> producer = Flux.create((sink) -> {
												  for(int i=1; i<=500 && !sink.isCancelled(); i++) {
												 	 log.info("generating: {}", i);
												 	 Util.sleep(Duration.ofMillis(50));
												 	 sink.next(i);
												  }
												  sink.complete();
											 })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.onBackpressureDrop()
				.log()
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	private static void lastStrategy() {
		Flux<Integer> producer = Flux.create((sink) -> {
									 		 	for(int i=1; i<=500 && !sink.isCancelled(); i++) {
									 		 		log.info("generating: {}", i);
									 		 		Util.sleep(Duration.ofMillis(50));
									 		 		sink.next(i);
									 		 	}
									 		 	sink.complete();
									 		 })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.onBackpressureLatest()
				.log()
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	private static void overflowStrategy() {
		Flux<Integer> producer = Flux.create((sink) -> {
									 		 	for(int i=1; i<=500 && !sink.isCancelled(); i++) {
									 		 		log.info("generating: {}", i);
									 		 		Util.sleep(Duration.ofMillis(50));
									 		 		sink.next(i);
									 		 	}
									 		 	sink.complete();
									 		 },
									 		 FluxSink.OverflowStrategy.BUFFER)
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.log()
				.limitRate(1)
				.publishOn(Schedulers.boundedElastic())
				.map(BackPressureStrategies::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
}
