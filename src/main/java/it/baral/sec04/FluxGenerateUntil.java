package it.baral.sec04;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

/**
 * Confronta due modi di terminare un {@code Flux.generate} al verificarsi di una
 * condizione sul valore emesso: gestendo la terminazione internamente al generatore
 * oppure delegandola all'operatore {@code takeUntil} applicato a valle.
 */
public class FluxGenerateUntil {

	/**
	 * Esegue la demo relativa a {@link #demo2()}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		demo2();
	}

	/**
	 * Genera nomi di paesi casuali e invoca {@code complete()} sul generatore stesso
	 * non appena viene emesso "Canada".
	 */
	private static void demo1() {
		Flux.generate(synchronousSink -> {
				String country = Util.faker().country().name();
				synchronousSink.next(country);
				if(country.equalsIgnoreCase("Canada")) {
					synchronousSink.complete();
				}
			})
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Genera nomi di paesi casuali senza mai completare il generatore, delegando
	 * la terminazione del flusso all'operatore {@code takeUntil} applicato a valle.
	 */
	private static void demo2() {
		Flux.<String>generate(synchronousSink -> {
				String country = Util.faker().country().name();
				synchronousSink.next(country);
			})
			.takeUntil(country -> country.equalsIgnoreCase("Canada"))
			.subscribe(Util.subscriber());
	}
}
