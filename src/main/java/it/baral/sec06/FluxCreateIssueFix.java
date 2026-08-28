package it.baral.sec06;

import it.baral.common.Util;
import it.baral.sec04.helper.NameGenerator;
import reactor.core.publisher.Flux;

/**
 * Mostra come {@code share()} risolva il problema di {@link it.baral.sec04.FluxCreateDownstreamDemand}
 * e simili: rendendo hot un {@code Flux.create} basato su {@link NameGenerator}, tutti i
 * sottoscrittori condividono la stessa sorgente e ricevono gli stessi valori generati manualmente.
 */
public class FluxCreateIssueFix {

	/**
	 * Sottoscrive due consumatori allo stesso {@code Flux} condiviso e genera manualmente
	 * 10 nomi, mostrando che entrambi li ricevono.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		NameGenerator nameGenerator = new NameGenerator();
		Flux<String> flux = Flux.create(nameGenerator).share();
		flux.subscribe(Util.subscriber("Sub1"));
		flux.subscribe(Util.subscriber("Sub2"));
		
		for(int i=0; i<10; i++) {
			nameGenerator.generate();
		}
	}
}
