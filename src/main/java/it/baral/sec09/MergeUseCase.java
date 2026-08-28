package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.helper.Kayak;

/**
 * Caso d'uso reale di {@code Flux.merge}: {@link Kayak} lo utilizza per aggregare
 * in tempo reale le offerte di pi&ugrave; compagnie aeree in un unico flusso di risultati.
 */
public class MergeUseCase {

	/**
	 * Sottoscrive il flusso aggregato di offerte di volo restituito da {@link Kayak}.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
		Kayak.getFlights()
			 .subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
}
