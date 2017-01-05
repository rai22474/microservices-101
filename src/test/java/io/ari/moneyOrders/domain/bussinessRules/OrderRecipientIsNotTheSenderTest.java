package io.ari.moneyOrders.domain.bussinessRules;

import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.businessRules.OrderRecipientIsNotTheSender;
import io.ari.moneyOrders.domain.recipients.Recipient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderRecipientIsNotTheSenderTest {

	@Test
	public void shouldNotReturnNullViolations() {
		assertNotNull(rule.isSatisfied(moneyOrder));
	}

	@Test
	public void shouldSatisfyWhenRecipientNotIsTheSender() {
		when(recipient.isTheSameAs(BUCKS_ID)).thenReturn(false);

		assertTrue(rule.isSatisfied(moneyOrder).isEmpty());
	}

	@Test
	public void shouldNotSatisfyWhenrecipientIsTheSender() {
		when(recipient.isTheSameAs(BUCKS_ID)).thenReturn(true);

		assertFalse(rule.isSatisfied(moneyOrder).isEmpty());
	}

	@Before
	public void setupMoneyOrder() {
		when(moneyOrder.getRecipient()).thenReturn(recipient);
		when(moneyOrder.getBucksId()).thenReturn(BUCKS_ID);
	}

	@InjectMocks
	private OrderRecipientIsNotTheSender rule;

	@Mock
	private MoneyOrder moneyOrder;

	@Mock
	private Recipient recipient;

	private static final String BUCKS_ID = "asd8asd80";
}