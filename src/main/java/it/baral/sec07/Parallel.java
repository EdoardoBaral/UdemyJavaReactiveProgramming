package it.baral.sec07;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * Dimostra l'elaborazione parallela di un {@link Flux} tramite {@code parallel()} e
 * {@code runOn(Schedulers.parallel())}, con successivo ricongiungimento in un unico
 * flusso sequenziale tramite {@code sequential()}.
 */
public class Parallel {

	private static final Logger log = LoggerFactory.getLogger(Parallel.class);

	/**
	 * Suddivide un {@link Flux} di interi in 4 "rail" paralleli, elabora ciascun
	 * valore con un'operazione dispendiosa in tempo e ricongiunge i risultati in un
	 * unico flusso sequenziale.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
		Flux.range(1, 10)
			.parallel(4)
			.runOn(Schedulers.parallel())
			.map(Parallel::process)
			.sequential()
			.map(i -> i +"a")
			.subscribe(Util.subscriber());

		Util.sleepSeconds(10);
	}

	/**
	 * Simula un'elaborazione dispendiosa in tempo (1 secondo) e raddoppia il valore
	 * ricevuto.
	 *
	 * @param i il valore da elaborare
	 * @return il valore raddoppiato
	 */
	private static int process(int i) {
		log.info("time consuming task {}", i);
		Util.sleepSeconds(1);
		return i*2;
	}
}
