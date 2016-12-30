package io.ari.customers.domain.exceptions;

public class CustomerExists extends Exception {

	public CustomerExists(String code, String description) {
		super(description);
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	private final String description;

	private final String code;

	private static final long serialVersionUID = -3200320723207531622L;
}
