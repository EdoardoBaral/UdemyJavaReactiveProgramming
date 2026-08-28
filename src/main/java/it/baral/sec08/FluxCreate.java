package it.baral.sec08;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
 * Dimostra il comportamento di backpressure di un {@link Flux} costruito con
 * {@code Flux.create}, generato a raffica su uno scheduler parallelo e consumato
 * lentamente su {@code boundedElastic}.
 */
public class FluxCreate {

	private static final Logger log = LoggerFactory.getLogger(FluxCreate.class);

	/**
	 * Genera fino a 500 interi tramite un {@link reactor.core.publisher.FluxSink},
	 * interrompendosi anticipatamente se il subscriber cancella la sottoscrizione,
	 * e li elabora lentamente per osservare l'effetto della richiesta (request-n).
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
		System.setProperty("reactor.bufferSize.small", "16");
		
		Flux<Integer> producer = Flux.create((sink) -> {
									     for(int i=1; i<=500 && !sink.isCancelled(); i++) {
										   log.info("generating: {}", i);
										   Util.sleep(Duration.ofMillis(50));
										   sink.next(i);
									     }
									     sink.complete();
								     })
									 .cast(Integer.class)
									 .subscribeOn(Schedulers.parallel());
		
		producer.publishOn(Schedulers.boundedElastic())
				.map(FluxCreate::timeConsumingTask)
				.subscribe(Util.subscriber());
		
		Util.sleepSeconds(60);
	}
	
	/**
	 * Simula un'elaborazione dispendiosa in tempo (1 secondo) sul valore ricevuto.
	 *
	 * @param i il valore da elaborare
	 * @return lo stesso valore ricevuto in ingresso
	 */
	private static int timeConsumingTask(int i) {
		log.info("received: {}", i);
		Util.sleepSeconds(1);
		return i;
	}
}
