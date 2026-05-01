package it.baral.sec03;

import it.baral.common.Util;
import it.baral.sec03.helper.NameGenerator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class FluxVSList {
	
	public static void main(String[] args) {
		AtomicInteger i = new AtomicInteger(1);
		
		System.out.println("Approccio con la lista");
		List<String> namesList = NameGenerator.generateNamesList(10);
		namesList.forEach(name -> System.out.println(i.getAndIncrement() +") "+name));
		System.out.println();
		
		System.out.println("Approccio con Flux");
		NameGenerator.generateNamesFlux(10)
					 .subscribe(Util.subscriber());
	}
}
