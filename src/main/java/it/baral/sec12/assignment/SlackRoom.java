package it.baral.sec12.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Rappresenta una stanza di chat che smista i {@link SlackMessage} tra i
 * membri iscritti, usando internamente un sink multicast a replay in modo
 * che ogni nuovo membro riceva anche i messaggi inviati prima della sua
 * iscrizione.
 */
public class SlackRoom {

	private static final Logger log = LoggerFactory.getLogger(SlackRoom.class);

	private final String name;
	private final Sinks.Many<SlackMessage> sink;
	private final Flux<SlackMessage> flux;

	/**
	 * Crea una nuova stanza con il nome indicato, inizializzando il sink
	 * multicast a replay usato per distribuire i messaggi ai membri.
	 *
	 * @param name il nome della stanza
	 */
	public SlackRoom(String name) {
		this.name = name;
		this.sink = Sinks.many().replay().all();
		this.flux = this.sink.asFlux();
	}

	/**
	 * Aggiunge un membro alla stanza: configura l'azione di invio del
	 * membro affinché pubblichi i messaggi nella stanza e lo sottoscrive
	 * alla ricezione dei messaggi altrui.
	 *
	 * @param member il membro da aggiungere alla stanza
	 */
	public void addMember(SlackMember member) {
		log.info("{} joined the room {}", member.getName(), this.name);
		member.setMessageConsumer(message -> this.postMessage(member.getName(), message));
		this.subscribeToRoomMessages(member);
	}

	/**
	 * Sottoscrive il membro indicato ai messaggi della stanza, escludendo
	 * i messaggi inviati dal membro stesso e formattando ogni messaggio
	 * per la consegna prima di passarlo a {@link SlackMember#receive(String)}.
	 *
	 * @param member il membro da sottoscrivere ai messaggi della stanza
	 */
	public void subscribeToRoomMessages(SlackMember member) {
		this.flux.filter(sm -> !sm.sender().equals(member.getName()))
			     .map(sm -> sm.formatForDelivery(member.getName()))
				 .subscribe(member::receive);
	}

	/**
	 * Pubblica un nuovo messaggio nella stanza emettendolo sul sink interno.
	 *
	 * @param sender il nome del membro mittente
	 * @param message il testo del messaggio da pubblicare
	 */
	private void postMessage(String sender, String message) {
		this.sink.tryEmitNext(new SlackMessage(sender, message));
	}
}
