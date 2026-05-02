package it.baral.sec03;

import it.baral.common.Util;
import it.baral.sec03.client.ExternalServiceClient;

public class NonBlockingStreamingMessages {
	
	public static void main(String[] args) {
		ExternalServiceClient client = new ExternalServiceClient();
		client.getNames()
			  .subscribe(Util.subscriber("Sub1"));
		client.getNames()
			  .subscribe(Util.subscriber("Sub2"));
		Util.sleepSeconds(6);
	}
}
