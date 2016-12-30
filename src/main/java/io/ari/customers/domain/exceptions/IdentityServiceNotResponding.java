package io.ari.customers.domain.exceptions;

public class IdentityServiceNotResponding extends RuntimeException {

	public IdentityServiceNotResponding(Exception e) {
		super(e);
	}

	private static final long serialVersionUID = -189387662025917821L;

}
