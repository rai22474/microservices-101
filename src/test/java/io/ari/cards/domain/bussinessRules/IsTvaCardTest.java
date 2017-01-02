package io.ari.cards.domain.bussinessRules;

import io.ari.cards.domain.Card;
import io.ari.cards.domain.businessRules.IsTvaCard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IsTvaCardTest {

	@Test
	public void shouldNotReturnNullViolations() {
		assertNotNull(rule.isSatisfied(card));
	}

	@Test
	public void shouldSatisfyWhenCardTypeIsTva() {
		when(card.isTva()).thenReturn(true);

		assertTrue(rule.isSatisfied(card).isEmpty());
	}

	@Test
	public void shouldNotSatisfyWhenCardTypeIsNotTva() {
		when(card.isTva()).thenReturn(false);

		assertFalse(rule.isSatisfied(card).isEmpty());
	}

	@InjectMocks
	private IsTvaCard rule;

	@Mock
	private Card card;

}