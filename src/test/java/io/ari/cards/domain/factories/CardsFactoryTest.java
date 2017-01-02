package io.ari.cards.domain.factories;

import io.ari.cards.domain.Card;
import io.ari.cards.domain.repositories.CardsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardsFactoryTest {

	@Test
	public void shouldReturnSavedDetailedCard() {
		Card returnedCard = cardsFactory.createCard(CUSTOMER_ID, CUSTOMER_NAME, CUSTOMER_LASTNAME, BANKING_SERVICE_AGREEMENT_ID, CARD_BANKING_SERVICE_ID, CARD_TYPE, CARD_PAN, CARD_IMAGE, CARD_STATUS);
		assertEquals("Returned card must be the saved one.", savedCard, returnedCard);
	}

	@Before
	public void setupCardsRepository() throws Exception {
		Card similarCard = new Card(CUSTOMER_ID, CUSTOMER_NAME, CUSTOMER_LASTNAME, BANKING_SERVICE_AGREEMENT_ID, CARD_TYPE, CARD_BANKING_SERVICE_ID, CARD_PAN, CARD_IMAGE, CARD_STATUS);
		when(cardsRepository.save(argThat(samePropertyValuesAs(similarCard)))).thenReturn(savedCard);
	}

	@InjectMocks
	private CardsFactory cardsFactory;

	@Mock
	private CardsRepository cardsRepository;

	@Mock
	private Card savedCard;

	private static final String CUSTOMER_ID = "d9a6df9a8d0a";

	private static final String CARD_TYPE = "tfa";

	private static final String CARD_BANKING_SERVICE_ID = "as76d9a8s7d";

	private static final String BANKING_SERVICE_AGREEMENT_ID = "ads870a7d0a0d79";

	private static final String CUSTOMER_NAME = "Alfred";

	private static final String CUSTOMER_LASTNAME = "Hitchcock";

	private static final String CARD_PAN = "1234123412341234";

	private static final String CARD_IMAGE = "image.png";

	private static final String CARD_STATUS = "inactive";

}