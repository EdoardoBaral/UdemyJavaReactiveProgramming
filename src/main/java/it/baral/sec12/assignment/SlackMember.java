package it.baral.sec12.assignment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * Rappresenta un membro di una {@link SlackRoom} nell'esercitazione sui
 * sink reattivi: può inviare messaggi alla stanza tramite {@link #says}
 * (delegando all'azione di invio configurata dalla stanza) e ricevere
 * messaggi tramite {@link #receive}.
 */
@RequiredArgsConstructor
@Getter
@Setter
public class SlackMember {

	private static final Logger log = LoggerFactory.getLogger(SlackMember.class);

	private final String name;

	private Consumer<String> messageConsumer;

	/**
	 * Invia un messaggio alla stanza, delegando all'azione di invio
	 * configurata da {@link SlackRoom#addMember(SlackMember)}.
	 *
	 * @param message il testo del messaggio da inviare
	 */
	public void says(String message) {
		this.messageConsumer.accept(message);
	}

	/**
	 * Riceve un messaggio già formattato per la consegna e lo logga.
	 *
	 * @param message il testo del messaggio ricevuto
	 */
	public void receive(String message) {
		log.info(message);
	}
}
