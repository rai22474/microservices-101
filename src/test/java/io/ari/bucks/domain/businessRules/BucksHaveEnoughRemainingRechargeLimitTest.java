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
public class BucksHaveEnoughRemainingRechargeLimitTest {

	@Test
	public void shouldSatisfyWhenRemainingRechargeLimitIsEnough() {
		assertTrue(rule.isSatisfied(bucks, val("5").eur().entity()).isEmpty());
	}

	@Test
	public void shouldNotSatisfyWhenRemainingRechargeLimitIsNotEnough() {
		assertFalse(rule.isSatisfied(bucks, val("20").eur().entity()).isEmpty());
	}

	@Before
	public void setupRemainingRechargeLimit() {
		when(bucks.getRemainingRechargeLimit()).thenReturn(val("10").eur().entity());
	}

	@InjectMocks
	private BucksHaveEnoughRemainingRechargeLimit rule;

	@Mock
	private Bucks bucks;

}