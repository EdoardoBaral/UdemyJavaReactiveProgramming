package it.baral.sec02;

import it.baral.common.Util;
import reactor.core.publisher.Mono;

public class FileServiceAssignment {
	
	private static final String FILE_NAME_1 = "FileServiceExample01.txt";
	private static final String FILE_NAME_2 = "FileServiceExample02.txt";
	private static final String FILE_NAME_3 = "FileServiceExample03.txt";
	private static final String FILE_NAME_4 = "FileServiceExample04.txt";
	private static final String FILE_NAME_5 = "FileServiceExample05.txt";
	
	private static final String FILE_CONTENT_1 = "Contenuto del file FileServiceExample01.txt da utilizzare per il compito della Lezione 36 del corso \"Mastering Java reactive programming (from scratch)\"";
	private static final String FILE_CONTENT_2 = "Contenuto del file FileServiceExample02.txt da utilizzare per il compito della Lezione 36 del corso \"Mastering Java reactive programming (from scratch)\"";
	private static final String FILE_CONTENT_3 = "Contenuto del file FileServiceExample03.txt da utilizzare per il compito della Lezione 36 del corso \"Mastering Java reactive programming (from scratch)\"";
	private static final String FILE_CONTENT_4 = "Contenuto del file FileServiceExample04.txt da utilizzare per il compito della Lezione 36 del corso \"Mastering Java reactive programming (from scratch)\"";
	private static final String FILE_CONTENT_5 = "Contenuto del file FileServiceExample05.txt da utilizzare per il compito della Lezione 36 del corso \"Mastering Java reactive programming (from scratch)\"";
	
	public static void main(String[] args) {
		FileService fileService = new FileServiceImpl();
		
		//Scrittura dei file
		fileService.write(FILE_NAME_1, FILE_CONTENT_1).subscribe(Util.subscriber());
		fileService.write(FILE_NAME_2, FILE_CONTENT_2).subscribe(Util.subscriber());
		fileService.write(FILE_NAME_3, FILE_CONTENT_3).subscribe(Util.subscriber());
		fileService.write(FILE_NAME_4, FILE_CONTENT_4).subscribe(Util.subscriber());
		fileService.write(FILE_NAME_5, FILE_CONTENT_5).subscribe(Util.subscriber());
		
		//Lettura dei file presenti nella cartella
		Mono<String> fileContent1 = fileService.read("FileServiceExample01.txt");
		Mono<String> filecontent2 = fileService.read("FileServiceExample02.txt");
		Mono<String> filecontent3 = fileService.read("FileServiceExample03.txt");
		Mono<String> filecontent4 = fileService.read("FileServiceExample04.txt");
		Mono<String> filecontent5 = fileService.read("FileServiceExample05.txt");
		
		fileContent1.subscribe(Util.subscriber());
		filecontent2.subscribe(Util.subscriber());
		filecontent3.subscribe(Util.subscriber());
		filecontent4.subscribe(Util.subscriber());
		filecontent5.subscribe(Util.subscriber());
		
		//Cancellazione dei file
		fileService.delete(FILE_NAME_1).subscribe(Util.subscriber());
		fileService.delete(FILE_NAME_2).subscribe(Util.subscriber());
		fileService.delete(FILE_NAME_3).subscribe(Util.subscriber());
		fileService.delete(FILE_NAME_4).subscribe(Util.subscriber());
		fileService.delete(FILE_NAME_5).subscribe(Util.subscriber());
	}
}
