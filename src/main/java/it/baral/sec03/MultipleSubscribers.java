package it.baral.sec03;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

/**
 * Classe dimostrativa della sezione 3 del corso: mostra come un {@link Flux}
 * "freddo" ripeta l'intera sequenza di emissione per ogni nuova
 * sottoscrizione, anche quando la catena di operatori applicata differisce
 * tra un subscriber e l'altro.
 */
public class MultipleSubscribers {

	/**
	 * Sottoscrive lo stesso {@link Flux} tre volte, applicando ogni volta
	 * operatori diversi ({@code filter}, {@code map}) sulla stessa sequenza
	 * di partenza.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);
		flux.subscribe(Util.subscriber("Sub1"));
		
		flux.filter(e -> e > 7)
			.subscribe(Util.subscriber("Sub2"));
		
		flux.filter(e -> e % 2 == 0)
			.map(e -> e +"a")
			.subscribe(Util.subscriber("Sub3"));
	}
}
