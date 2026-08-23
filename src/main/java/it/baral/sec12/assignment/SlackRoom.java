package it.baral.sec12.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class SlackRoom {
	
	private static final Logger log = LoggerFactory.getLogger(SlackRoom.class);
	
	private final String name;
	private final Sinks.Many<SlackMessage> sink;
	private final Flux<SlackMessage> flux;
	
	public SlackRoom(String name) {
		this.name = name;
		this.sink = Sinks.many().replay().all();
		this.flux = this.sink.asFlux();
	}
	
	public void addMember(SlackMember member) {
		log.info("{} joined the room {}", member.getName(), this.name);
		member.setMessageConsumer(message -> this.postMessage(member.getName(), message));
		this.subscribeToRoomMessages(member);
	}
	
	public void subscribeToRoomMessages(SlackMember member) {
		this.flux.filter(sm -> !sm.sender().equals(member.getName()))
			     .map(sm -> sm.formatForDelivery(member.getName()))
				 .subscribe(member::receive);
	}
	
	private void postMessage(String sender, String message) {
		this.sink.tryEmitNext(new SlackMessage(sender, message));
	}
}
