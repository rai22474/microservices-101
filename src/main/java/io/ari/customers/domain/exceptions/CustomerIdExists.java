package io.ari.customers.domain.exceptions;

public class CustomerIdExists extends CustomerExists {

	public CustomerIdExists(String customerId) {
		super("existingIdentifier", "Attempting to create a customer with an existing identifier:" + customerId);
	}

	private static final long serialVersionUID = 4600247112218602419L;
}
