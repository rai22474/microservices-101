package io.ari.cards.domain.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.assemblers.HypermediaAssembler;
import io.ari.cards.domain.Card;
import io.ari.cards.resources.assemblers.CardsAssembler;
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
public class CardsAssemblerEntityToDtoTest {

	@Test
	public void shouldNotReturnNull() {
		Map<String, Object> returnedDto = cardsAssembler.convertEntityToDto(card);

		assertNotNull("Returned dto cannot be null.", returnedDto);
	}

	@Test
	public void shouldHaveALinksItem() {
		Map<String, Object> returnedDto = cardsAssembler.convertEntityToDto(card);

		assertTrue("Returned dto must have a _links element.", returnedDto.containsKey("_links"));
	}

	@Test
	public void shouldHaveASelfLink() {
		Map<String, Object> expectedLinks = ImmutableMap.of("a link", "myself");
		when(hypermediaAssembler.createHypermedia("api/cards/" + CARD_ID, "ari-read")).thenReturn(expectedLinks);

		Map<String, Object> returnedDto = cardsAssembler.convertEntityToDto(card);

		Map<String, Object> links = (Map<String, Object>) returnedDto.get("_links");
		assertEquals("Returned dto self link must be the expected.", expectedLinks, links);
	}

	@Test
	public void shouldHaveId() {
		Map<String, Object> returnedDto = cardsAssembler.convertEntityToDto(card);

		assertTrue("Returned dto must have an id element.", returnedDto.containsKey("id"));
		assertEquals("Returned dto must have the right id.", CARD_ID, returnedDto.get("id"));
	}

	@Test
	public void shouldHaveType() {
		Map<String, Object> returnedDto = cardsAssembler.convertEntityToDto(card);

		assertTrue("Returned dto must have an type element.", returnedDto.containsKey("type"));
		assertEquals("Returned dto must have the right type.", CARD_TYPE, returnedDto.get("type"));
	}

	@Before
	public void setupCard() {
		when(card.getId()).thenReturn(CARD_ID);
		when(card.getType()).thenReturn(CARD_TYPE);
		when(card.getStatus()).thenReturn(Card.Status.ACTIVE);

	}

	@InjectMocks
	private CardsAssembler cardsAssembler;

	@Mock
	private HypermediaAssembler hypermediaAssembler;

	@Mock
	private Card card;

	private static final String CARD_ID = "ad9f86a9d6a9";

	private static final String CARD_TYPE = "tfa";
}