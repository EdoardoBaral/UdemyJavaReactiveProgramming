package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Stream;

/**
 * Classe dimostrativa della sezione 3 del corso: mostra la creazione di un
 * {@link Flux} a partire da uno {@link Stream} tramite
 * {@link Flux#fromStream(Stream)}, evidenziando che uno stream Java, essendo
 * consumabile una sola volta, non puo' essere riutilizzato per piu'
 * sottoscrizioni e va quindi ricreato ad ogni utilizzo.
 */
public class FluxFromStream {

	/**
	 * Mostra l'errore causato dal riutilizzo dello stesso {@link Stream} gia'
	 * consumato e la soluzione corretta di crearne uno nuovo per ogni
	 * sottoscrizione.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		List<Integer> list = List.of(1, 2, 3, 4, 5);
		Stream<Integer> stream = list.stream();
		
		Flux.fromStream(stream)
			.subscribe(Util.subscriber("Sub1"));
		
		//Non posso costruire un Flux a partire da uno stream già chiuso, devo crearne uno nuovo ogni volta
		Flux.fromStream(stream)
			.subscribe(Util.subscriber("Sub2"));
		
		//Soluzione ottimale, apro un nuovo stream ogni volta che voglio creare un Flux
		Flux.fromStream(list.stream())
			.subscribe(Util.subscriber("Sub3"));
		Flux.fromStream(list.stream())
			.subscribe(Util.subscriber("Sub4"));
	}
}
