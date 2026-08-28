package it.baral.sec04;

import it.baral.common.Util;
import it.baral.sec04.helper.NameGenerator;
import reactor.core.publisher.Flux;

/**
 * Versione rifattorizzata dell'esempio {@code Flux.create}: la logica di generazione
 * viene estratta in {@link NameGenerator}, un {@link java.util.function.Consumer} riutilizzabile
 * che mantiene un riferimento al {@link reactor.core.publisher.FluxSink} per emettere valori su richiesta esterna.
 */
public class FluxCreateRefactor {

	/**
	 * Crea un {@code Flux} tramite {@link NameGenerator} e genera manualmente 10 nomi
	 * chiamando ripetutamente {@link NameGenerator#generate()}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		NameGenerator nameGenerator = new NameGenerator();
		Flux<String> flux = Flux.create(nameGenerator);
		flux.subscribe(Util.subscriber("Sub1"));
		
		for(int i=0; i<10; i++) {
			nameGenerator.generate();
		}
	}
}
