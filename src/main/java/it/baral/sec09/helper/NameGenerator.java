package it.baral.sec09.helper;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

/**
 * Genera nomi casuali uno alla volta (con un secondo di ritardo simulato) e ne
 * mantiene una cache, riproposta ai nuovi subscriber tramite {@code startWith}
 * prima di continuare con la generazione di nuovi nomi.
 */
public class NameGenerator {

	private static final Logger log = LoggerFactory.getLogger(NameGenerator.class);

	private final List<String> cache = new ArrayList<>();

	/**
	 * Restituisce un {@link Flux} che riproduce subito i nomi gi&agrave; presenti in
	 * cache e poi genera nuovi nomi casuali, aggiungendo ciascuno alla cache condivisa.
	 *
	 * @return un {@link Flux} di nomi
	 */
	public Flux<String> generateNames() {
		return Flux.generate(sink -> {
								log.info("generating name");
								Util.sleepSeconds(1);
								String name = Util.faker().name().fullName();
								cache.add(name);
								sink.next(name);
							})
				   .startWith(cache)
				   .cast(String.class);
	}
}
