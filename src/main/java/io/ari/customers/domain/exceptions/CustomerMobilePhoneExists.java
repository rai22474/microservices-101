package io.ari.customers.domain.exceptions;

public class CustomerMobilePhoneExists extends CustomerExists {

	public CustomerMobilePhoneExists(String mobilePhone) {
		super("existingMobilePhone", "Attempting to create a customer with an existing mobile phone:" + mobilePhone);
	}

	private static final long serialVersionUID = -7849614983844984163L;
}
