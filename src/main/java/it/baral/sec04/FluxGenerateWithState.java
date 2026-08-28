package it.baral.sec04;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

/**
 * Mostra la variante di {@code Flux.generate} con stato: uno stato iniziale (un contatore)
 * viene passato e aggiornato a ogni invocazione del generatore, permettendo di terminare
 * il flusso in base sia al valore generato sia al numero di elementi prodotti.
 */
public class FluxGenerateWithState {

	/**
	 * Genera nomi di paesi casuali finché non viene emesso "canada" oppure vengono
	 * prodotti 10 elementi, usando un contatore come stato del generatore.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux.generate(() -> 0, (counter, sink) -> {
			String country = Util.faker().country().name();
			sink.next(country);
			counter++;
			if(country.equalsIgnoreCase("canada") || counter == 10) {
				sink.complete();
			}
			return counter;
		}).subscribe(Util.subscriber());
	}
}
