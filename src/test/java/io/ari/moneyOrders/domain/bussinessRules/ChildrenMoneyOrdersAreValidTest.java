package io.ari.moneyOrders.domain.bussinessRules;

import com.google.common.collect.ImmutableList;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.moneyOrders.domain.businessRules.ChildrenMoneyOrdersAreValid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChildrenMoneyOrdersAreValidTest {

	@Test
	public void shouldBeSatisfiedWhenAllChildrenAreValid() {
		when(businessRulesValidator.validate(firstMoneyOrder)).thenReturn(ImmutableList.of());
		when(businessRulesValidator.validate(secondMoneyOrder)).thenReturn(ImmutableList.of());
		when(moneyOrderBundle.getOrders()).thenReturn(ImmutableList.of(firstMoneyOrder, secondMoneyOrder));

		assertTrue(rule.isSatisfied(moneyOrderBundle).isEmpty());
	}

	@Test
	public void shouldNotBeSatisfiedWhenAnyChildrenAreNotValid() {
		when(businessRulesValidator.validate(firstMoneyOrder)).thenReturn(ImmutableList.of());
		when(businessRulesValidator.validate(secondMoneyOrder)).thenReturn(ImmutableList.of(mock(Violation.class), mock(Violation.class)));
		when(moneyOrderBundle.getOrders()).thenReturn(ImmutableList.of(firstMoneyOrder, secondMoneyOrder));

		Collection<Violation> returnedViolations = rule.isSatisfied(moneyOrderBundle);

		assertFalse("It must exist some violations.", returnedViolations.isEmpty());
		assertEquals("It must return 1 violation.", 1, returnedViolations.size());
	}

	@Test
	public void shouldBeSatisfiedWhenNoChildren() {
		when(moneyOrderBundle.getOrders()).thenReturn(ImmutableList.of());

		assertTrue(rule.isSatisfied(moneyOrderBundle).isEmpty());
	}

	@InjectMocks
	private ChildrenMoneyOrdersAreValid rule;

	@Mock
	private MoneyOrderBundle moneyOrderBundle;

	@Mock
	private MoneyOrder firstMoneyOrder;

	@Mock
	private MoneyOrder secondMoneyOrder;

	@Mock
	private BusinessRulesValidator<MoneyOrder> businessRulesValidator;
}