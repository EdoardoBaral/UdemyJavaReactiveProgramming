package it.baral.sec04;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

/**
 * Dimostra l'uso di {@code Flux.create}, un operatore che consente di generare
 * eventi in modo push (spinti dal produttore) tramite un {@link reactor.core.publisher.FluxSink},
 * anche in modo bilanciato rispetto alla richiesta del sottoscrittore.
 */
public class FluxCreate {

	/**
	 * Esegue tre esempi di creazione di un {@code Flux} con {@code Flux.create}:
	 * emissione di valori fissi, emissione di valori generati casualmente e
	 * un ciclo che termina al verificarsi di una condizione.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux.create(fluxSink -> {
				fluxSink.next(1);
				fluxSink.next(2);
				fluxSink.complete();
			})
			.subscribe(Util.subscriber("Sub1"));
		
		Flux.create(fluxSink -> {
				for(int i=0; i<10; i++) {
					fluxSink.next(Util.faker().country().name());
				}
				fluxSink.complete();
			})
			.subscribe(Util.subscriber("Sub2"));
		
		Flux.create(fluxSink -> {
				String countryName = "";
				do {
					countryName = Util.faker().country().name();
					fluxSink.next(countryName);
				} while(!"Canada".equals(countryName));
				fluxSink.complete();
			})
			.subscribe(Util.subscriber("Sub3"));
	}
}
