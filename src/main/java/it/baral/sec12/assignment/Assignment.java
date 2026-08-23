package it.baral.sec12.assignment;

import it.baral.common.Util;

public class Assignment {
	
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
