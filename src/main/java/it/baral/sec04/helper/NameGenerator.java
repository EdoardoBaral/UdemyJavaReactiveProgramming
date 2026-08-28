package it.baral.sec04.helper;

import it.baral.common.Util;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

/**
 * Consumer usato come callback di {@code Flux.create} per mantenere un riferimento
 * al {@link FluxSink} e permettere l'emissione di nomi casuali in un momento successivo,
 * su comando esterno (es. da un altro thread).
 */
public class NameGenerator implements Consumer<FluxSink<String>> {

	private FluxSink<String> fluxSink;

	/**
	 * Memorizza il {@link FluxSink} fornito da {@code Flux.create} per un uso successivo.
	 *
	 * @param stringFluxSink il sink su cui emettere i valori del {@code Flux}
	 */
	@Override
	public void accept(FluxSink<String> stringFluxSink) {
		this.fluxSink = stringFluxSink;
	}

	/**
	 * Genera un nome completo casuale e lo emette sul {@link FluxSink} memorizzato.
	 */
	public void generate() {
		this.fluxSink.next(Util.faker().name().fullName());
	}
}
