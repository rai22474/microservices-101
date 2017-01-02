package io.ari.cards.domain.bussinessRules;

import io.ari.cards.domain.Card;
import io.ari.cards.domain.businessRules.IsActiveCard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IsActiveCardTest {

	@Test
	public void shouldNotReturnNullViolations() {
		assertNotNull(rule.isSatisfied(card));
	}

	@Test
	public void shouldSatisfyWhenStatusIsActive() {
		when(card.isActive()).thenReturn(true);

		assertTrue(rule.isSatisfied(card).isEmpty());
	}

	@Test
	public void shouldNotSatisfyWhenStatusIsNotActive() {
		when(card.isActive()).thenReturn(false);

		assertFalse(rule.isSatisfied(card).isEmpty());
	}

	@InjectMocks
	private IsActiveCard rule;

	@Mock
	private Card card;

}