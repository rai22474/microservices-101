package io.ari.moneyRequests.domain.businessRules;

import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.moneyRequests.domain.MoneyRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestRecipientIsNotTheSenderTest {

	@Test
	public void shouldNotReturnNullViolations() {
		assertNotNull(rule.isSatisfied(moneyRequest));
	}

	@Test
	public void shouldSatisfyWhenRecipientNotIsTheSender() {
		when(recipient.isTheSameAs(BUCKS_ID)).thenReturn(false);

		assertTrue(rule.isSatisfied(moneyRequest).isEmpty());
	}

	@Test
	public void shouldNotSatisfyWhenrecipientIsTheSender() {
		when(recipient.isTheSameAs(BUCKS_ID)).thenReturn(true);

		assertFalse(rule.isSatisfied(moneyRequest).isEmpty());
	}

	@Before
	public void setupMoneyOrder() {
		when(moneyRequest.getRecipient()).thenReturn(recipient);
		when(moneyRequest.getBucksId()).thenReturn(BUCKS_ID);
	}

	@InjectMocks
	private RequestRecipientIsNotTheSender rule;

	@Mock
	private MoneyRequest moneyRequest;

	@Mock
	private Recipient recipient;

	private static final String BUCKS_ID = "af690f6a90f";
}