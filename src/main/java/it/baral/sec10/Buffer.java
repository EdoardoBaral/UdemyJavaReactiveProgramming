package it.baral.sec10;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Dimostra le diverse varianti dell'operatore {@code buffer}: raggruppa gli elementi
 * emessi da un {@link Flux} in liste, in base al numero di elementi, a un intervallo
 * di tempo, o a entrambi ({@code bufferTimeout}).
 */
public class Buffer {

	/**
	 * Punto di ingresso: esegue la demo di {@code bufferTimeout} (le altre varianti
	 * sono disponibili ma commentate).
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		bufferDemo();
//		bufferWithDimensionDemo();
//		bufferWithDurationDemo();
//		bufferWithDimensionAndInfiniteFluxDemo();
		bufferTimeoutDemo();

		Util.sleepSeconds(5);
	}

	/**
	 * Raggruppa tutti gli elementi del flusso in un'unica lista, emessa al completamento.
	 */
	private static void bufferDemo() {
		eventStream().buffer()
					 .subscribe(Util.subscriber());
	}

	/**
	 * Raggruppa gli elementi del flusso in liste di dimensione fissa (3 elementi).
	 */
	private static void bufferWithDimensionDemo() {
		eventStream().buffer(3)
					 .subscribe(Util.subscriber());
	}

	/**
	 * Raggruppa gli elementi del flusso in liste emesse a intervalli di tempo fissi (500ms).
	 */
	private static void bufferWithDurationDemo() {
		eventStream().buffer(Duration.ofMillis(500))
					 .subscribe(Util.subscriber());
	}

	/**
	 * Raggruppa gli elementi di un flusso infinito in liste di dimensione fissa (3 elementi).
	 */
	private static void bufferWithDimensionAndInfiniteFluxDemo() {
		eventStreamInfinite().buffer(3)
							 .subscribe(Util.subscriber());
	}

	/**
	 * Raggruppa gli elementi di un flusso infinito in liste emesse al raggiungimento
	 * di 3 elementi oppure trascorso 1 secondo, a seconda di quale condizione si verifica prima.
	 */
	private static void bufferTimeoutDemo() {
		eventStreamInfinite().bufferTimeout(3, Duration.ofSeconds(1))
			.subscribe(Util.subscriber());
	}

	/**
	 * Genera un flusso finito di 10 eventi, uno ogni 200 millisecondi.
	 *
	 * @return un {@code Flux} di stringhe che rappresentano gli eventi generati
	 */
	private static Flux<String> eventStream() {
		return Flux.interval(Duration.ofMillis(200))
				   .take(10)
				   .map(i -> "event "+ (i+1));
	}

	/**
	 * Genera un flusso di 10 eventi seguiti da un flusso infinito che non emette più nulla,
	 * utile per dimostrare gli operatori di buffering senza completare mai la sorgente.
	 *
	 * @return un {@code Flux} infinito di stringhe che rappresentano gli eventi generati
	 */
	private static Flux<String> eventStreamInfinite() {
		return Flux.interval(Duration.ofMillis(200))
				   .take(10)
				   .concatWith(Flux.never())
				   .map(i -> "event "+ (i+1));
	}
}
