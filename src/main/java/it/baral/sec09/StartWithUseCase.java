package it.baral.sec09;

import it.baral.common.Util;
import it.baral.sec09.helper.NameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartWithUseCase {
	
	private static final Logger log = LoggerFactory.getLogger(StartWithUseCase.class);
	
	public static void main(String[] args) {
		NameGenerator nameGenerator = new NameGenerator();
		
		nameGenerator.generateNames()
					 .take(2)
					 .subscribe(Util.subscriber("sub1"));
		
		nameGenerator.generateNames()
					 .take(2)
					 .subscribe(Util.subscriber("sub2"));
		
		nameGenerator.generateNames()
					 .take(3)
					 .subscribe(Util.subscriber("sub3"));
	}
}
