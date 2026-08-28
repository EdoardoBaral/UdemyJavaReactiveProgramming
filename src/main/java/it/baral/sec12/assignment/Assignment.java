package it.baral.sec12.assignment;

import it.baral.common.Util;

/**
 * Esercitazione sui sink reattivi: simula una chat room Slack in cui i
 * membri si uniscono in momenti diversi e si scambiano messaggi tramite
 * {@link SlackRoom}, verificando che chi si unisce successivamente non
 * riceva i messaggi già inviati in precedenza.
 */
public class Assignment {

	/**
	 * Punto di ingresso dell'applicazione: crea una stanza, aggiunge membri
	 * in momenti diversi e simula lo scambio di messaggi tra loro.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
		SlackRoom room = new SlackRoom("reactor");
		
		SlackMember sam = new SlackMember("sam");
		SlackMember jake = new SlackMember("jake");
		SlackMember mike = new SlackMember("mike");
		
		room.addMember(sam);
		room.addMember(jake);
		
		sam.says("Hi all...");
		Util.sleepSeconds(2);
		
		jake.says("Hey!");
		sam.says("I simply wanted to say hi...");
		Util.sleepSeconds(4);
		
		room.addMember(mike);
		mike.says("Hey guys, glad to be here");
	}
}
