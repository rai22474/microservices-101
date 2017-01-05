package io.ari.moneyOrders.domain.bussinessRules;

import com.google.common.collect.ImmutableSet;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.businessRules.RecipientCanReceive;
import io.ari.moneyOrders.domain.recipients.Recipient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static io.ari.money.MoneyBuilder.val;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecipientCanReceiveTest {

	@Test
	public void shouldSatisfyWhenRecipientCanReceiveAmount() {
		when(moneyOrder.getRecipient()).thenReturn(recipient);
		when(moneyOrder.getAmount()).thenReturn(amount);

		when(businessRulesValidator.validate(recipient,amount)).thenReturn(ImmutableSet.of());

		assertTrue(rule.isSatisfied(moneyOrder).isEmpty());
	}

	@Test
	public void shouldNotSatisfyWhenRecipientCannotReceiveAmount() {
		when(moneyOrder.getRecipient()).thenReturn(recipient);
		when(moneyOrder.getAmount()).thenReturn(amount);

		when(businessRulesValidator.validate(recipient,amount)).thenReturn(ImmutableSet.of(mock(Violation.class)));

		assertFalse(rule.isSatisfied(moneyOrder).isEmpty());
	}

	@InjectMocks
	private RecipientCanReceive rule;

	@Mock
	private MoneyOrder moneyOrder;

	@Mock
	private Recipient recipient;

	@Mock
	private BusinessRulesValidator<Recipient> businessRulesValidator;

	private Money amount = val("100").eur().entity();

}