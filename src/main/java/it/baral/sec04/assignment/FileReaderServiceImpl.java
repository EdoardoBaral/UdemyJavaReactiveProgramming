package it.baral.sec04.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReaderServiceImpl implements FileReaderService{
	
	private static final Logger log = LoggerFactory.getLogger(FileReaderServiceImpl.class);
	
	@Override
	public Flux<String> readFile(Path path) {
		return Flux.generate(() -> openFile(path),
							 this::readFile,
							 this::closeFile);
	}
	
	private BufferedReader openFile(Path path) throws IOException {
		log.info("Opening file");
		return Files.newBufferedReader(path);
	}
	
	private BufferedReader readFile(BufferedReader reader, SynchronousSink<String> sink) {
		try {
			String line =  reader.readLine();
			log.info("Reading line: " + line);
			if(line == null) {
				sink.complete();
			} else {
				sink.next(line);
			}
		} catch(Exception e) {
			sink.error(e);
		}
		
		return reader;
	}
	
	private void closeFile(BufferedReader reader) {
		try {
			reader.close();
			log.info("File closed");
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
