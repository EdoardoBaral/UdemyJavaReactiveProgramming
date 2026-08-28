package it.baral.sec04;

import it.baral.common.Util;
import it.baral.sec01.subscriber.SubscriberImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 * Confronta due strategie di produzione con {@code Flux.create}: la produzione
 * rispettosa della richiesta a valle (tramite {@code onRequest}) e la produzione
 * anticipata di tutti gli elementi, indipendentemente dalla domanda del sottoscrittore.
 */
public class FluxCreateDownstreamDemand {

	private static final Logger log = LoggerFactory.getLogger(FluxCreateDownstreamDemand.class);

	/**
	 * Esegue la demo di produzione su richiesta ({@link #produceOnDemand()}).
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		produceOnDemand();
		//produceEarly();
	}

	/**
	 * Genera nomi solo quando il sottoscrittore ne richiede esplicitamente, tramite
	 * la callback {@code onRequest} del {@link reactor.core.publisher.FluxSink}, e mostra
	 * l'effetto di richieste parziali e della cancellazione della sottoscrizione.
	 */
	public static void produceOnDemand() {
		SubscriberImpl subscriber = new SubscriberImpl();
		Flux.<String>create(fluxSink -> {
				fluxSink.onRequest(request -> {
					for(int i=0; i<request && !fluxSink.isCancelled(); i++) {
						String name = Util.faker().name().fullName();
						log.info("Generated: {}", name);
						fluxSink.next(name);
					}
				});
			})
			.subscribe(subscriber);
		
		Util.sleepSeconds(2);
		subscriber.getSubscription().request(2);
		Util.sleepSeconds(2);
		subscriber.getSubscription().request(2);
		subscriber.getSubscription().cancel();
		
		Util.sleepSeconds(2);
		subscriber.getSubscription().request(2);
	}
	
	/**
	 * Genera immediatamente tutti i 10 nomi senza attendere la richiesta del sottoscrittore,
	 * mostrando come questo comportamento ignori il backpressure imposto a valle.
	 */
	private static void produceEarly() {
		SubscriberImpl subscriber = new SubscriberImpl();
		Flux.<String>create(fluxSink -> {
				for(int i=0; i<10; i++) {
					String name = Util.faker().name().fullName();
					log.info("Generated: {}", name);
					fluxSink.next(name);
				}
				fluxSink.complete();
			})
			.subscribe(subscriber);
		
		Util.sleepSeconds(2);
		subscriber.getSubscription().request(2);
		Util.sleepSeconds(2);
		subscriber.getSubscription().request(2);
		subscriber.getSubscription().cancel();
		
		Util.sleepSeconds(2);
		subscriber.getSubscription().request(2);
	}
}
