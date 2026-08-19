package it.baral.sec11;

public class ServerError extends RuntimeException {
	
	public ServerError() {
		super("server error");
	}
}
