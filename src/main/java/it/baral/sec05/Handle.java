package it.baral.sec05;

import it.baral.common.Util;
import reactor.core.publisher.Flux;

/**
 * Introduce l'operatore {@code handle}, che permette di trasformare, filtrare o
 * terminare (con errore) un {@code Flux} elemento per elemento tramite un
 * {@link reactor.core.publisher.SynchronousSink} esplicito.
 */
public class Handle {

	/**
	 * Esegue la demo relativa a {@link #assignment()}.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		handle();
		assignment();
	}

	/**
	 * Mostra {@code handle} usato per trasformare, scartare o far fallire elementi
	 * in base al loro valore.
	 */
	private static void handle() {
		Flux.range(1, 10)
//			.filter(i -> i != 7)
			.handle((item, sink) -> {
				switch(item) {
					case 1 -> sink.next(-2);
					case 4 -> {}
					case 7 -> sink.error(new Exception("ooops"));
					default -> sink.next(item);
				}
			})
			.cast(Integer.class)
			.subscribe(Util.subscriber());
	}
	
	/**
	 * Genera nomi di paesi casuali e usa {@code handle} per completare il flusso
	 * non appena viene emesso "Canada".
	 */
	private static void assignment() {
		Flux.generate(sink -> {
				String country = Util.faker().country().name();
				sink.next(country);
			})
			.cast(String.class)
			.handle((item, sink) -> {
				sink.next(item);
				if(item.equalsIgnoreCase("Canada")) {
					sink.complete();
				}
			})
			.subscribe(Util.subscriber());
	}
}
