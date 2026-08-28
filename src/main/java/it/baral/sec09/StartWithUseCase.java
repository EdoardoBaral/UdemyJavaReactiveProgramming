package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.helper.NameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Caso d'uso reale di {@code startWith}: {@link NameGenerator} lo utilizza per
 * "riprodurre" ai nuovi subscriber i nomi gi&agrave; generati in precedenza prima di
 * continuare con la generazione di nuovi valori.
 */
public class StartWithUseCase {

	private static final Logger log = LoggerFactory.getLogger(StartWithUseCase.class);

	/**
	 * Sottoscrive tre subscriber in sequenza allo stesso {@link NameGenerator},
	 * mostrando come ciascuno riceva prima i nomi gi&agrave; presenti in cache e poi
	 * eventuali nuovi nomi generati.
	 *
	 * @param args argomenti da linea di comando, non utilizzati
	 */
	public static void main(String[] args) {
		NameGenerator nameGenerator = new NameGenerator();
		
		nameGenerator.generateNames()
					 .take(2)
					 .subscribe(Util.subscriber("sub1"));
		
		nameGenerator.generateNames()
					 .take(2)
					 .subscribe(Util.subscriber("sub2"));
		
		nameGenerator.generateNames()
					 .take(3)
					 .subscribe(Util.subscriber("sub3"));
	}
}
