package it.baral.sec12.assignment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
@Setter
public class SlackMember {
	
	private static final Logger log = LoggerFactory.getLogger(SlackMember.class);
	
	private final String name;
	
	private Consumer<String> messageConsumer;

	public void says(String message) {
		this.messageConsumer.accept(message);
	}
	
	public void receive(String message) {
		log.info(message);
	}
}
