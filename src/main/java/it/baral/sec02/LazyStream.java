package it.baral.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

public class LazyStream {
	
	private static final Logger log = LoggerFactory.getLogger(LazyStream.class);
	
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
