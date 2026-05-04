package it.baral.sec04.helper;

import it.baral.common.Util;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

public class NameGenerator implements Consumer<FluxSink<String>> {
	
	private FluxSink<String> fluxSink;
	
	@Override
	public void accept(FluxSink<String> stringFluxSink) {
		this.fluxSink = stringFluxSink;
	}
	
	public void generate() {
		this.fluxSink.next(Util.faker().name().fullName());
	}
}
