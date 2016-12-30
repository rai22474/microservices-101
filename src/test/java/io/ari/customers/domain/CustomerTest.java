package io.ari.customers.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CustomerTest {

	@Test
	public void shouldAssignIdAndIdCard() {
		Customer customer = new Customer(ID, ID_CARD);

		assertNotNull("Created customer must have id.", customer.getId());
		assertEquals("Created customer must have the right id.", ID, customer.getId());
		assertNotNull("Created customer must have idCard.", customer.getIdCard());
		assertEquals("Created customer must have the right idCard.", ID_CARD, customer.getIdCard());
	}

	private static final String ID = "da976a97d59a7d";

	private static final String ID_CARD = "11111111H";

}