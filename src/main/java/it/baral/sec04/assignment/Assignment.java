package it.baral.sec04.assignment;

import it.baral.common.Util;

import java.nio.file.Path;

public class Assignment {
	
	public static void main(String[] args) {
	
		Path path = Path.of("src/main/resources/sec04/file.txt");
		FileReaderService fileReaderService = new FileReaderServiceImpl();
		fileReaderService.readFile(path)
//						 .take(10)
//						 .takeUntil(l -> l.equalsIgnoreCase("Riga 010"))
						 .subscribe(Util.subscriber());
	}
}
