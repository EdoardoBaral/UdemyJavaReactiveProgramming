package it.baral.common;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.LoopResources;

/**
 * Classe base per i client HTTP di esempio del progetto: incapsula la creazione
 * di un {@link HttpClient} di Reactor Netty configurato con un pool di risorse
 * di rete dedicato e un URL base comune, cosi' le sottoclassi devono solo
 * comporre le chiamate specifiche.
 */
public abstract class AbstractHttpClient {

	private static final String BASE_URL = "http://localhost:7070";
	protected final HttpClient httpClient;

	/**
	 * Costruisce il client HTTP condiviso, creando un {@link LoopResources}
	 * dedicato e configurando l'{@link HttpClient} con l'URL base del servizio
	 * di esempio.
	 */
	public AbstractHttpClient() {
		LoopResources loopResources = LoopResources.create("baral", 1, true);
		this.httpClient = HttpClient.create().runOn(loopResources).baseUrl(BASE_URL);
	}
}
