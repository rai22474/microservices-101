package io.ari.bucks.domain.businessRules;

import io.ari.bucks.domain.Bucks;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static io.ari.money.MoneyBuilder.val;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BucksHaveEnoughAvailableTest {

	@Test
	public void shouldSatisfyWhenAvailableIsEnough() {
		assertTrue(rule.isSatisfied(bucks, val("100.10").eur().entity()).isEmpty());
	}

	@Test
	public void shouldNotSatisfyWhenAvailableIsNotEnough() {
		assertFalse(rule.isSatisfied(bucks, val("400.10").eur().entity()).isEmpty());
	}

	@Before
	public void setupBucks() {
		when(bucks.getAvailableBalance()).thenReturn(val("200.0").eur().entity());
	}

	@InjectMocks
	private BucksHaveEnoughAvailable rule;

	@Mock
	private Bucks bucks;

}