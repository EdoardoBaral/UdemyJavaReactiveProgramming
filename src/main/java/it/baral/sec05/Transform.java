package it.baral.sec05;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Mostra l'operatore {@code transform}, che consente di applicare in modo condizionale
 * e riutilizzabile una catena di operatori (qui un debugger basato sui callback {@code do*})
 * a un {@code Flux}, decidendo la trasformazione al momento della composizione.
 */
public class Transform {

	private static final Logger log = LoggerFactory.getLogger(Transform.class);

	/** Rappresenta un cliente con identificativo e nome, usato negli esempi. */
	record Customer(int id, String name) {}
	/** Rappresenta un ordine di acquisto con nome del prodotto, prezzo e quantità, usato negli esempi. */
	record PurchaseOrder(String name, int price, int quantity) {}

	/**
	 * Applica condizionalmente il debugger, tramite {@code transform}, a un {@code Flux}
	 * di clienti e a un {@code Flux} di ordini, a seconda del flag di debug.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		boolean isDebugEnabled = true;
		
		getCustomers().transform(isDebugEnabled ? addDebugger() : Function.identity())
					  .subscribe(Util.subscriber());
		
		System.out.println();
		
		isDebugEnabled = false;
		getPurchaseOrders().transform(isDebugEnabled ? addDebugger() : Function.identity())
						   .subscribe(Util.subscriber());
	}
	
	/**
	 * Crea un {@code Flux} di 3 clienti con nomi casuali.
	 *
	 * @return un {@code Flux} di {@link Customer}
	 */
	private static Flux<Customer> getCustomers() {
		return Flux.range(1, 3)
				   .map(i -> new Customer(i, Util.faker().name().fullName()));
	}
	
	/**
	 * Crea un {@code Flux} di 5 ordini di acquisto con prodotto, prezzo e quantità casuali.
	 *
	 * @return un {@code Flux} di {@link PurchaseOrder}
	 */
	private static Flux<PurchaseOrder> getPurchaseOrders() {
		return Flux.range(1, 5)
				   .map(i -> new PurchaseOrder(Util.faker().commerce().productName(),
													  Integer.parseInt(Util.faker().commerce().price().replaceAll("[^\\d]", "")),
											   		  ThreadLocalRandom.current().nextInt(1, 11)));
	}
	
	/**
	 * Costruisce un operatore riutilizzabile che aggiunge il logging di ogni elemento,
	 * del completamento e degli errori, da applicare a un {@code Flux} tramite {@code transform}.
	 *
	 * @param <T> il tipo di elemento emesso dal {@code Flux}
	 * @return un {@link UnaryOperator} che decora il {@code Flux} con i callback di logging
	 */
	private static <T> UnaryOperator<Flux<T>> addDebugger() {
		return flux -> flux.doOnNext(i -> log.info("received: {}", i))
								   .doOnComplete(() -> log.info("completed"))
								   .doOnError(err -> log.error("error: ", err));
	}
}
