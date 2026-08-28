package it.baral.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

/**
 * Classe dimostrativa della sezione 2 del corso: mostra la valutazione lazy
 * degli {@link Stream} di Java, confrontandola con il modello reattivo. Uno
 * stream senza operazione terminale non esegue le operazioni intermedie
 * (es. {@code peek}); solo l'invocazione di un'operazione terminale (es.
 * {@code toList}) innesca l'elaborazione degli elementi.
 */
public class LazyStream {

	private static final Logger log = LoggerFactory.getLogger(LazyStream.class);

	/**
	 * Esegue quattro casi che mostrano quando uno {@link Stream} valuta
	 * effettivamente le operazioni intermedie definite su di esso.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		System.out.println("--- Caso 1: Stream senza operazione terminale");
		Stream.of(1)
			  .peek(i -> log.info("Peeked value: {}", i));
		System.out.println();
		
		System.out.println("--- Caso 2: Stream con operazione terminale");
		Stream.of(1)
			  .peek(i -> log.info("Peeked value: {}", i))
			  .toList();
		System.out.println();
		
		System.out.println("--- Caso 3: Stream con operazione terminale e stampa del risultato");
		List<String> list = Stream.of("xyz")
								  .peek(i -> log.info("Peeked value: {}", i))
								  .toList();
		System.out.println(list);
		
		System.out.println("--- Caso 4: Stream con elementi multipli, con operazione terminale e stampa del risultato");
		List<String> list2 = Stream.of("x", "y", "z")
								   .peek(i -> log.info("Peeked value: {}", i))
								   .toList();
		System.out.println(list2);
	}
}
