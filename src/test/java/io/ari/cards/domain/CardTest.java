package io.ari.cards.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CardTest {

	@Test
	public void shouldReturnBlockedCard() {
		card.setStatus("blocked");

		assertTrue("the card must be blocked", card.isBlocked());
	}

	@Test
	public void shouldReturnNonBlockedCard() {
		card.setStatus("asdadasd");

		assertFalse("the card must not be blocked", card.isBlocked());
	}

	@Test
	public void shouldChangeStatusToBlocked() {
		card.block();

		assertEquals("Card status must be changed to blocked.", "blocked", card.getStatus());
	}

	@Test
	public void shouldReturnActiveCard() {
		card.setStatus("active");

		assertTrue("the card must be active", card.isActive());
	}

	@Test
	public void shouldReturnNonActiveCard() {
		card.setStatus("asdadasd");

		assertFalse("the card must not be active", card.isActive());
	}


	@InjectMocks
	private Card card = new Card("", "", "", BANKING_SERVICE_AGREEMENT_ID, "tva", BANKING_SERVICE_CARD_ID, "", "", "");

	private static final String BANKING_SERVICE_AGREEMENT_ID = "d9ba79da9db0";

	private static final String BANKING_SERVICE_CARD_ID = "b0abd97a9da0d";

}
