package io.ari.cards.domain.repositories.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.ari.cards.domain.Card;
import io.ari.repositories.assemblers.JacksonStorageAssembler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardsStorageAssemblerEntityToDtoTest {

	@Test
	public void shouldReturnDtoWithJacksonProperties() {
		Map<String, Object> returnedCardDto = cardsStorageAssembler.convertEntityToDto(card);

		assertNotNull("Returned card dto cannot be null.", returnedCardDto);
		cardDto.forEach((key, value) -> {
			assertTrue("Returned card dto must contain all jackson dto properties.", returnedCardDto.containsKey(key));
			assertEquals("Returned card dto must contain the right " + key, value, returnedCardDto.get(key));
		});
	}

	@Test
	public void shouldReturnDtoWithType() {
		Map<String, Object> returnedCardDto = cardsStorageAssembler.convertEntityToDto(card);

		assertTrue("Returned card dto must contain a type.", returnedCardDto.containsKey("type"));
		assertEquals("Returned card dto must contain the right type", "card", returnedCardDto.get("type"));
	}

	@Test
	public void shouldReturnDtoWithParticipantId() {
		Map<String, Object> returnedCardDto = cardsStorageAssembler.convertEntityToDto(card);

		assertTrue("Returned card dto must contain a participants", returnedCardDto.containsKey("participants"));
		assertEquals("Returned card dto must contain the right participants", ImmutableList.of(ImmutableMap.of("id", CUSTOMER_ID)), returnedCardDto.get("participants"));
	}

	@Before
	public void setupCard() {
		when(card.getCustomerId()).thenReturn(CUSTOMER_ID);

		when(jacksonStorageAssembler.convertEntityToDto(card)).thenReturn(cardDto);
	}

	@InjectMocks
	private CardsStorageAssembler cardsStorageAssembler;

	@Mock
	private JacksonStorageAssembler<Card> jacksonStorageAssembler;

	@Mock
	private Card card;

	private Map<String, Object> cardDto = ImmutableMap.of("a", "b", "c", "d");

	private static final String CUSTOMER_ID = "f08a680fa058a0f";

}