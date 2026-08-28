package it.baral.sec10;

import it.baral.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Dimostra l'operatore {@code window}: a differenza di {@code buffer}, suddivide il flusso
 * sorgente in sotto-flussi ({@link Flux} annidati) invece che in liste, permettendo
 * di elaborarli in modo reattivo.
 */
public class Window {

	/**
	 * Punto di ingresso della demo: suddivide il flusso di eventi in finestre di 5 elementi
	 * ed elabora ciascuna finestra tramite {@code flatMap}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		eventStream().window(5)
					 .flatMap(Window::processEvents)
					 .subscribe();

		Util.sleepSeconds(60);
	}

	/**
	 * Genera un flusso infinito di eventi, uno ogni 200 millisecondi.
	 *
	 * @return un {@code Flux} di stringhe che rappresentano gli eventi generati
	 */
	private static Flux<String> eventStream() {
		return Flux.interval(Duration.ofMillis(200))
				   .map(i -> "event "+ (i+1));
	}

	/**
	 * Elabora una singola finestra di eventi stampando un asterisco per ogni elemento
	 * e una riga vuota al completamento della finestra.
	 *
	 * @param flux la finestra (sotto-flusso) di eventi da elaborare
	 * @return un {@code Mono<Void>} che completa quando la finestra è stata elaborata interamente
	 */
	private static Mono<Void> processEvents(Flux<String> flux) {
		return flux.doOnNext(e -> System.out.print("*"))
				   .doOnComplete(System.out::println)
				   .then();
	}
}
