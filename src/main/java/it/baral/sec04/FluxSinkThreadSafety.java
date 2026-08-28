package it.baral.sec04;

import it.baral.common.Util;
import it.baral.sec04.helper.NameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

/**
 * Confronta l'accesso concorrente non sincronizzato a una struttura dati con l'uso
 * thread-safe di {@link reactor.core.publisher.FluxSink}, che serializza automaticamente
 * le emissioni provenienti da più thread.
 */
public class FluxSinkThreadSafety {

	private static final Logger log = LoggerFactory.getLogger(FluxSinkThreadSafety.class);

	/**
	 * Esegue in sequenza la demo non thread-safe e quella thread-safe.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		demoNotThreadSafe();
		demoThreadSafe();
	}

	/**
	 * Mostra la perdita di elementi quando 10 thread scrivono concorrentemente
	 * su una {@link ArrayList} non sincronizzata.
	 */
	private static void demoNotThreadSafe() {
		ArrayList<Integer> list = new ArrayList<>();
		Runnable runnable = () -> {
			for(int i=0; i<1000; i++) {
				list.add(i);
			}
		};
		
		for(int i=0; i<10; i++) {
			Thread.ofPlatform().start(runnable);
		}
		
		Util.sleepSeconds(3);
		log.info("List size: {}", list.size());
	}
	
	/**
	 * Mostra come, generando i valori tramite {@link NameGenerator} e {@code Flux.create},
	 * nessun elemento venga perso nonostante l'emissione concorrente da 10 thread,
	 * grazie alla serializzazione interna del {@link reactor.core.publisher.FluxSink}.
	 */
	private static void demoThreadSafe() {
		ArrayList<String> list = new ArrayList<>();
		NameGenerator nameGenerator = new NameGenerator();
		Flux<String> flux = Flux.create(nameGenerator);
		flux.subscribe(list::add);
		
		Runnable runnable = () -> {
			for(int i=0; i<1000; i++) {
				nameGenerator.generate();
			}
		};
		
		for(int i=0; i<10; i++) {
			Thread.ofPlatform().start(runnable);
		}
		
		Util.sleepSeconds(3);
		log.info("List size: {}", list.size());
	}
}
