package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.helper.Kayak;

public class MergeUseCase {
	
	public static void main(String[] args) {
		Kayak.getFlights()
			 .subscribe(Util.subscriber());
		
		Util.sleepSeconds(5);
	}
}
