package it.baral.sec04.assignment;

import reactor.core.publisher.Flux;

import java.nio.file.Path;

public interface FileReaderService {
	
	Flux<String> readFile(Path path);
}
