package it.baral.sec07;

import it.baral.common.Util;
import it.baral.sec07.client.ExternalServiceClient;

public class EventLoopIssueFix {
	
	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();
		
		for(int i=1; i<=5; i++) {
			client.getProductName(i)
				  .map(EventLoopIssueFix::process)
				  .subscribe(Util.subscriber());
		}
		Util.sleepSeconds(20);
	}
	
	private static String process(String input) {
		Util.sleepSeconds(1);
		return input +"-processed";
	}
}
