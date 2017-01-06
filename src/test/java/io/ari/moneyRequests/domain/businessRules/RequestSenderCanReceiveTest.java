package io.ari.moneyRequests.domain.businessRules;

import com.google.common.collect.ImmutableSet;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.repositories.exceptions.EntityNotFound;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RequestSenderCanReceiveTest {

	@Test
	public void shouldReturnBucksCanReceiveViolations() throws EntityNotFound {
		when(moneyRequestBundle.getBucksId()).thenReturn(BUCKS_ID);
		when(moneyRequestBundle.calculateAmount()).thenReturn(amount);
		when(bucksRepository.findById(BUCKS_ID)).thenReturn(bucks);

		Collection<Violation> violations = ImmutableSet.of(mock(Violation.class));
		when(bucksReceptionValidator.validate(bucks,amount)).thenReturn(violations);

		Collection<Violation> returnedViolations = rule.isSatisfied(moneyRequestBundle);

		assertEquals("Returned violations must be the returned by canReceive method.", violations, returnedViolations);
	}

	@InjectMocks
	private RequestSenderCanReceive rule;

	@Mock
	private MoneyRequestBundle moneyRequestBundle;

	@Mock
	private Bucks bucks;

	@Mock
	private Money amount;

	@Mock
	private BusinessRulesValidator<Bucks> bucksReceptionValidator;

	@Mock
	private BucksRepository bucksRepository;

	private String BUCKS_ID ="1";
}