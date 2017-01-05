package io.ari.bucks.domain.businessRules;

import io.ari.bucks.domain.Bucks;
import io.ari.cards.domain.Card;
import io.ari.cards.domain.repositories.CardsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TvaIsEnabledTest {

	@Test
	public void shouldHaveACollectionOfViolations() {
		assertNotNull("Cannot return null violations", rule.isSatisfied(bucks));
	}

	@Test
	public void shouldNotSatisfiedWhenTvaIsBlocked() {
		when(card.isBlocked()).thenReturn(true);

		assertFalse("the rule must not satisfy when tva is blocked", rule.isSatisfied(bucks).isEmpty());
	}

	@Test
	public void shouldSatisfyWhenTvaIsNotBlocked() {
		when(card.isBlocked()).thenReturn(false);

		assertTrue("the rule must not satisfy when tva is blocked", rule.isSatisfied(bucks).isEmpty());
	}

	@Before
	public void setupBucks() {
		when(bucks.getCustomerId()).thenReturn("asdf");
		when(cardsRepository.findByCustomerIdAndType(bucks.getCustomerId(), "tva")).thenReturn(card);
	}

	@InjectMocks
	private TvaIsEnabled rule;

	@Mock
	private Bucks bucks;

	@Mock
	private CardsRepository cardsRepository;

	@Mock
	private Card card;
}