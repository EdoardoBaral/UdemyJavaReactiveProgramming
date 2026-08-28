package it.baral.sec07;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

/**
 * Dimostra il comportamento di default di un {@link Flux} quando pi&ugrave; subscriber
 * si iscrivono senza alcun operatore di scheduling: ogni subscribe viene eseguito
 * sul thread chiamante.
 */
public class DefaultBehaviorDemo {

	private static final Logger log = LoggerFactory.getLogger(DefaultBehaviorDemo.class);

	/**
	 * Crea un {@link Flux} e vi iscrive due subscriber da due thread separati, per
	 * mostrare che, senza {@code subscribeOn}/{@code publishOn}, ciascuna sottoscrizione
	 * viene eseguita interamente sul thread che ha invocato {@code subscribe}.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
		Flux<Integer> flux = Flux.create(sink -> {
								 	for(int i=0; i<3; i++) {
								 		log.info("generating: {}", i);
								 		sink.next(i);
								 	}
								 	sink.complete();
								 })
								 .cast(Integer.class)
								 .doOnNext(v -> log.info("value: {}", v));
		
		Runnable runnable1 = () -> flux.subscribe(Util.subscriber("sub1"));
		Runnable runnable2 = () -> flux.subscribe(Util.subscriber("sub2"));
		
		Thread.ofPlatform().start(runnable1);
		Thread.ofPlatform().start(runnable2);
	}
}
