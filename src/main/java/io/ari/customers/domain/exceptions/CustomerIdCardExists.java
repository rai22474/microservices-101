package io.ari.customers.domain.exceptions;

public class CustomerIdCardExists extends CustomerExists {

	public CustomerIdCardExists(String idCard) {
		super("existingIdCard", "Attempting to create a customer with an existing idCard:" + idCard);
	}

	private static final long serialVersionUID = -7651556871893634334L;
}
