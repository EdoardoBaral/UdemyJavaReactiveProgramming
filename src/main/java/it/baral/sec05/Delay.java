package it.baral.sec05;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Mostra l'operatore {@code delayElements}, che ritarda l'emissione di ciascun
 * elemento del {@code Flux} della durata indicata.
 */
public class Delay {

	/**
	 * Emette gli interi da 1 a 10 con un ritardo di 1 secondo tra un elemento e l'altro,
	 * attendendo poi 12 secondi per lasciare completare l'emissione asincrona.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		
		Flux.range(1, 10)
			.log()
			.delayElements(Duration.ofSeconds(1))
			.subscribe(Util.subscriber());
		
		Util.sleepSeconds(12);
	}
}
