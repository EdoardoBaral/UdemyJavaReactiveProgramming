package it.baral.sec07;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Parallel {
	
	private static final Logger log = LoggerFactory.getLogger(Parallel.class);
	
	public static void main(String[] args) {
		Flux.range(1, 10)
			.parallel(4)
			.runOn(Schedulers.parallel())
			.map(Parallel::process)
			.sequential()
			.map(i -> i +"a")
			.subscribe(Util.subscriber());
		
		Util.sleepSeconds(10);
	}
	
	private static int process(int i) {
		log.info("time consuming task {}", i);
		Util.sleepSeconds(1);
		return i*2;
	}
}
