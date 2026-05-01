package it.baral.sec03.helper;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.IntStream;

public class NameGenerator {
	
	public static List<String> generateNamesList(int count) {
		return IntStream.rangeClosed(1, count)
				   		.mapToObj(x -> generateNames())
				   		.toList();
	}
	
	public static Flux<String> generateNamesFlux(int count) {
		return Flux.range(1, count)
				   .map(x -> generateNames());
	}
	
	private static String generateNames() {
		Util.sleepSeconds(1);
		return Util.faker().name().fullName();
	}
}
