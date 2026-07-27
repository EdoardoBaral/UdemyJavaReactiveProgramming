package it.baral.sec06;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class HotPublisher {
	
	private static final Logger log = LoggerFactory.getLogger(HotPublisher.class);
	
	public static void main(String[] args) {
		Flux<String> movieFlux = movieStream().share();
		//refCount(1) è un'istruzione equivalente a share() e indica che il publisher deve avere almeno un subscriber prima di iniziare ad emettere elementi
		//Flux<String> movieFlux = movieStream().refCount(1);
		Util.sleepSeconds(2);
		movieFlux.subscribe(Util.subscriber("sub1"));
		
		Util.sleepSeconds(3);
		movieFlux.take(3)
				 .subscribe(Util.subscriber("sub2"));
		
		Util.sleepSeconds(15);
	}
	
	private static Flux<String> movieStream() {
		return Flux.generate(() -> {
					   log.info("request received");
					   return 1;
				   },
				   (state, sink) -> {
					   String scene = "movie scene"+ state;
					   log.info("playing scene {}", scene);
					   sink.next(scene);
					   return ++state;
				   })
				   .take(10)
				   .delayElements(Duration.ofSeconds(1))
				   .cast(String.class);
	}
}
