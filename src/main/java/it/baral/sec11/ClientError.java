package it.baral.sec11;

public class ClientError extends RuntimeException {

	public ClientError() {
		super("bad request");
	}
}
