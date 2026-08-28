package it.baral.sec12.assignment;

/**
 * Rappresenta un messaggio inviato in una {@link SlackRoom}, con il nome
 * del mittente e il testo del messaggio.
 *
 * @param sender il nome del membro che ha inviato il messaggio
 * @param message il testo del messaggio
 */
public record SlackMessage(String sender, String message) {

	private static final String MESSAGE_FORMAT = "[%s -> %s] : %s";

	/**
	 * Formatta il messaggio per la consegna a un destinatario specifico,
	 * indicando mittente e destinatario.
	 *
	 * @param receiver il nome del membro destinatario del messaggio
	 * @return la stringa formattata pronta per essere recapitata
	 */
	public String formatForDelivery(String receiver) {
		return String.format(MESSAGE_FORMAT, this.sender(), receiver, this.message());
	}
}
