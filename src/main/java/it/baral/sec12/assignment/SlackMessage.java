package it.baral.sec12.assignment;

public record SlackMessage(String sender, String message) {
	
	private static final String MESSAGE_FORMAT = "[%s -> %s] : %s";
	
	public String formatForDelivery(String receiver) {
		return String.format(MESSAGE_FORMAT, this.sender(), receiver, this.message());
	}
}
