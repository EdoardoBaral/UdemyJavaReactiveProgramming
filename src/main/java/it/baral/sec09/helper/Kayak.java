package it.baral.sec09.helper;

import reactor.core.publisher.Flux;

import java.time.Duration;

public class Kayak {
	
	public static Flux<Flight> getFlights() {
		return Flux.merge(Emirates.getFlights(), QatarAirways.getFlights(), AmericanAirlines.getFlights())
				   .take(Duration.ofSeconds(2));
	}
}
