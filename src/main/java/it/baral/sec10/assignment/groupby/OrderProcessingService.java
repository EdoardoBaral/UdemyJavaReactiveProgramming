package it.baral.sec10.assignment.groupby;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Servizio di supporto per l'esercizio sull'operatore {@code groupBy} (sezione sec10):
 * associa a ciascuna categoria di prodotto gestita una logica di elaborazione specifica
 * da applicare al relativo sotto-flusso di ordini.
 */
public class OrderProcessingService {

	private static final Map<String, UnaryOperator<Flux<PurchaseOrder>>> PROCESSOR_MAP = Map.of("Automotive", automotiveProcessing(),
																							"Kids", kidsProcessing());

	/**
	 * Restituisce la logica di elaborazione per la categoria "Automotive": aumenta il prezzo
	 * di ogni ordine di 100.
	 *
	 * @return un operatore che trasforma il flusso di ordini della categoria "Automotive"
	 */
	private static UnaryOperator<Flux<PurchaseOrder>> automotiveProcessing() {
		return flux -> flux.map(po -> new PurchaseOrder(po.item(), po.category(), po.price() + 100));
	}

	/**
	 * Restituisce la logica di elaborazione per la categoria "Kids": per ogni ordine
	 * aggiunge, prima dell'ordine stesso, un ordine gratuito omaggio.
	 *
	 * @return un operatore che trasforma il flusso di ordini della categoria "Kids"
	 */
	private static UnaryOperator<Flux<PurchaseOrder>> kidsProcessing() {
		return flux -> flux.flatMap(po -> getFreeKidsOrder(po).flux()
											  												  .startWith(po));
	}

	/**
	 * Genera l'ordine gratuito omaggio corrispondente a un ordine della categoria "Kids".
	 *
	 * @param order ordine originale per cui generare l'omaggio
	 * @return un {@code Mono} che emette il nuovo ordine gratuito
	 */
	private static Mono<PurchaseOrder> getFreeKidsOrder(PurchaseOrder order) {
		return Mono.fromSupplier(() -> new PurchaseOrder(order.item() + "-FREE", order.category(), 0));
	}

	/**
	 * Restituisce un predicato che verifica se esiste una logica di elaborazione registrata
	 * per la categoria dell'ordine fornito.
	 *
	 * @return un {@code Predicate} che indica se un ordine può essere elaborato
	 */
	public static Predicate<PurchaseOrder> canProcess() {
		return po -> PROCESSOR_MAP.containsKey(po.category());
	}

	/**
	 * Restituisce la logica di elaborazione registrata per la categoria indicata.
	 *
	 * @param category categoria di prodotto per cui recuperare la logica di elaborazione
	 * @return l'operatore di trasformazione associato alla categoria
	 */
	public static UnaryOperator<Flux<PurchaseOrder>> getProcessor(String category) {
		return PROCESSOR_MAP.get(category);
	}
}
