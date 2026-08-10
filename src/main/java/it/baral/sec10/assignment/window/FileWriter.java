package it.baral.sec10.assignment.window;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class FileWriter {
	
	private final Path path;
	
	private BufferedWriter writer;
	
	private void createFile() {
		try {
			this.writer = Files.newBufferedWriter(path);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void closeFile() {
		try {
			this.writer.close();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void write(String content) {
		try {
			this.writer.write(content);
			this.writer.newLine();
			this.writer.flush();
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Mono<Void> create(Flux<String> flux, Path path) {
		FileWriter fileWriter = new FileWriter(path);
		return flux.doOnNext(fileWriter::write)
				   .doFirst(fileWriter::createFile)
				   .doFinally(s -> fileWriter.closeFile())
				   .then();
	}
}
