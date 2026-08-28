package it.baral.sec03;

import it.baral.common.Util;
import it.baral.sec03.helper.NameGenerator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe dimostrativa della sezione 3 del corso: confronta l'approccio
 * "eager" con {@link List} (che attende la generazione di tutti gli elementi
 * prima di poterli usare) con l'approccio reattivo tramite {@link reactor.core.publisher.Flux}
 * fornito da {@link NameGenerator} (che emette ogni elemento non appena
 * disponibile).
 */
public class FluxVSList {

	/**
	 * Genera dieci nomi con entrambi gli approcci (lista e {@link reactor.core.publisher.Flux}) e ne
	 * stampa/logga il risultato, per confrontarne i tempi di risposta.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		AtomicInteger i = new AtomicInteger(1);
		
		System.out.println("Approccio con la lista");
		List<String> namesList = NameGenerator.generateNamesList(10);
		namesList.forEach(name -> System.out.println(i.getAndIncrement() +") "+name));
		System.out.println();
		
		System.out.println("Approccio con Flux");
		NameGenerator.generateNamesFlux(10)
					 .subscribe(Util.subscriber());
	}
}
