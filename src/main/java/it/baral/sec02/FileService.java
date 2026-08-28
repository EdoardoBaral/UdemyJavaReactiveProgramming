package it.baral.sec02;

import reactor.core.publisher.Mono;

/**
 * Contratto reattivo per un servizio di accesso al file system, usato nella
 * sezione 2 del corso per dimostrare come esporre operazioni di I/O
 * (potenzialmente bloccanti) tramite {@link Mono}.
 */
public interface FileService {

	/**
	 * Legge il contenuto testuale del file indicato.
	 *
	 * @param fileName il nome del file da leggere
	 * @return un {@link Mono} con il contenuto del file
	 */
	Mono<String> read(String fileName);

	/**
	 * Scrive il contenuto indicato nel file specificato, creandolo o
	 * sovrascrivendolo.
	 *
	 * @param fileName il nome del file su cui scrivere
	 * @param content il contenuto testuale da scrivere nel file
	 * @return un {@link Mono} vuoto che completa a scrittura avvenuta
	 */
	Mono<Void> write(String fileName, String content);

	/**
	 * Elimina il file indicato.
	 *
	 * @param fileName il nome del file da eliminare
	 * @return un {@link Mono} vuoto che completa a eliminazione avvenuta
	 */
	Mono<Void> delete(String fileName);
}
