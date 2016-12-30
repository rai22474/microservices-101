package io.ari.customers.domain.exceptions;


public class IdentityServiceBadResponse extends RuntimeException {

	private static final long serialVersionUID = 5791205795536939402L;

	public IdentityServiceBadResponse(String response){
		super(response);
	}

}
