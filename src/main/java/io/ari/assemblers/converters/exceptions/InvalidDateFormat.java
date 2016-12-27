package io.ari.assemblers.converters.exceptions;


public class InvalidDateFormat extends RuntimeException {

	private static final long serialVersionUID = 2750166126446384058L;

	public InvalidDateFormat(Exception e) {
		super(e);
	}
}
