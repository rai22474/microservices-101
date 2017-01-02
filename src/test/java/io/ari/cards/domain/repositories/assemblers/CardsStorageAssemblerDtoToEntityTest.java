package io.ari.cards.domain.repositories.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.cards.domain.Card;
import io.ari.repositories.assemblers.JacksonStorageAssembler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardsStorageAssemblerDtoToEntityTest {

	@Test
	public void shouldReturnTheRightCard() {
		Map<String, Object> cardDto = ImmutableMap.of("card", "aCard");
		when(jacksonStorageAssembler.convertDtoToEntity(cardDto)).thenReturn(card);

		Card returnedEntity = cardsStorageAssembler.convertDtoToEntity(cardDto);

		assertNotNull("Returned entity cannot be null.", returnedEntity);
		assertEquals("Returned entity must be the assembled by jackson assembler.", card, returnedEntity);
	}

	@InjectMocks
	private CardsStorageAssembler cardsStorageAssembler;

	@Mock
	private JacksonStorageAssembler<Card> jacksonStorageAssembler;

	@Mock
	private Card card;

}