package it.baral.sec12;

import it.baral.common.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

/**
 * Classe dimostrativa che illustra l'utilizzo di {@link Sinks.One}, un sink
 * reattivo capace di emettere al massimo un singolo segnale (un valore, un
 * completamento senza valore, oppure un errore) verso uno o più subscriber.
 * <p>
 * A differenza di un {@link Mono} costruito con i metodi statici classici,
 * un {@link Sinks.One} permette di produrre il segnale in modo imperativo,
 * anche dopo che i subscriber si sono già sottoscritti.
 */
public class SinkOne {
	
	private static final Logger log = LoggerFactory.getLogger(SinkOne.class);

	/**
	 * Punto di ingresso dell'applicazione. Decommentare uno dei metodi
	 * di demo per eseguire il relativo scenario.
	 *
	 * @param args argomenti da linea di comando (non utilizzati)
	 */
	public static void main(String[] args) {
//		demoSinkOne();
//		demoSinkOneEmpty();
//		demoSinkOneError();
//		demoSinkOneMultipleSubscribers();
		demoSinkOneEmitValue();
	}

	/**
	 * Crea un {@link Sinks.One}, vi si sottoscrive tramite un {@link Mono}
	 * e infine emette con successo il valore {@code "Hello"}.
	 */
	private static void demoSinkOne() {
		Sinks.One<Object> sink = Sinks.one();
		Mono<Object> mono = sink.asMono();
		mono.subscribe(Util.subscriber());
		sink.tryEmitValue("Hello");
	}

	/**
	 * Crea un {@link Sinks.One}, vi si sottoscrive tramite un {@link Mono}
	 * e infine lo completa senza emettere alcun valore.
	 */
	private static void demoSinkOneEmpty() {
		Sinks.One<Object> sink = Sinks.one();
		Mono<Object> mono = sink.asMono();
		mono.subscribe(Util.subscriber());
		sink.tryEmitEmpty();
	}

	/**
	 * Crea un {@link Sinks.One}, vi si sottoscrive tramite un {@link Mono}
	 * e infine lo termina emettendo un errore.
	 */
	private static void demoSinkOneError() {
		Sinks.One<Object> sink = Sinks.one();
		Mono<Object> mono = sink.asMono();
		mono.subscribe(Util.subscriber());
		sink.tryEmitError(new RuntimeException("ooops"));
	}

	/**
	 * Crea un {@link Sinks.One} e vi sottoscrive due subscriber distinti
	 * ("Sam" e "Mike") prima di emettere il valore {@code "Hello"},
	 * mostrando come entrambi ricevano lo stesso segnale.
	 */
	private static void demoSinkOneMultipleSubscribers() {
		Sinks.One<Object> sink = Sinks.one();
		Mono<Object> mono = sink.asMono();
		mono.subscribe(Util.subscriber("Sam"));
		mono.subscribe(Util.subscriber("Mike"));
		sink.tryEmitValue("Hello");
	}
	
	/**
	 * Crea un {@link Sinks.One}, vi si sottoscrive tramite un {@link Mono}
	 * ed emette il valore {@code "Hello"} tramite {@code emitValue}, che a
	 * differenza di {@code tryEmitValue} accetta un {@link Sinks.EmitFailureHandler}
	 * invocato in caso di fallimento dell'emissione, permettendo di
	 * loggare il tipo di segnale e l'esito dell'emissione.
	 */
	private static void demoSinkOneEmitValue() {
		Sinks.One<Object> sink = Sinks.one();
		Mono<Object> mono = sink.asMono();
		mono.subscribe(Util.subscriber("Sam"));
		
		sink.emitValue("hi", ((signalType, emitResult) -> {
			log.info("hi");
			log.info(signalType.name());
			log.info(emitResult.name());
			return false;
		}));
		
		//Questo secondo tentativo di emissione fallirà perché il sink ha già emesso un valore, quindi il gestore di fallimento loggherà l'esito.
		sink.emitValue("hello", ((signalType, emitResult) -> {
									    	log.info("hello");
									    	log.info(signalType.name());
									    	log.info(emitResult.name());
									    	return false;
								       }));
	}
}
