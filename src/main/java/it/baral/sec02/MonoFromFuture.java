package it.baral.sec02;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

/**
 * Classe dimostrativa della sezione 2 del corso: mostra {@link Mono#fromFuture(CompletableFuture)},
 * che adatta una {@link CompletableFuture} gia' esistente (o in corso di
 * esecuzione) a un {@link Mono}, emettendone il risultato quando disponibile.
 */
public class MonoFromFuture {

	private static final Logger log = LoggerFactory.getLogger(MonoFromFuture.class);

	/**
	 * Sottoscrive un {@link Mono} creato a partire da una
	 * {@link CompletableFuture} asincrona, attendendo poi qualche secondo
	 * affinche' il thread principale non termini prima che il risultato sia
	 * disponibile.
	 *
	 * @param args argomenti da riga di comando (non utilizzati)
	 * @throws InterruptedException se l'attesa finale viene interrotta
	 */
	public static void main(String[] args) throws InterruptedException{
		Mono.fromFuture(getName())
			.subscribe(Util.subscriber());

		Util.sleepSeconds(5);
	}

	/**
	 * Genera in modo asincrono un nome completo fittizio.
	 *
	 * @return una {@link CompletableFuture} che completera' con il nome generato
	 */
	private static CompletableFuture<String> getName() {
		return CompletableFuture.supplyAsync(() -> {
			log.info("Generating name");
			return Util.faker().name().fullName();
		});
	}
}
