package it.baral.sec10.assignment.window;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class WindowAssignment {
	
	public static void main(String[] args) {
		AtomicInteger counter = new AtomicInteger(0);
		String fileNameFormat = "src/main/resources/sec10/file%d.txt";
		
		eventStream().window(5)
			.flatMap(flux -> FileWriter.create(flux, Path.of(fileNameFormat.formatted(counter.incrementAndGet()))))
			.subscribe();
		
		Util.sleepSeconds(60);
	}
	
	private static Flux<String> eventStream() {
		return Flux.interval(Duration.ofMillis(200))
				   .map(i -> "event "+ (i+1));
	}
}
