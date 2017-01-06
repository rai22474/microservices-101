package io.ari.moneyRequests.domain.businessRules;

import com.google.common.collect.ImmutableList;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.moneyRequests.domain.MoneyRequest;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChildrenMoneyRequestsAreValidTest {

	@Test
	public void shouldReturnEmptyViolationsWhenNoChildren() {
		Collection<Violation> returnedViolations = rule.isSatisfied(moneyRequestBundle);

		assertNotNull("Returned violations cannot be null.", returnedViolations);
		assertTrue("Returned violations must be empty.", returnedViolations.isEmpty());
	}

	@Test
	public void shouldReturnOneMoneyRequestViolations() {
		MoneyRequest firstMoneyRequest = createRequestWithViolations(mock(Violation.class));
		MoneyRequest secondMoneyRequest = createRequestWithViolations();
		MoneyRequest thirdMoneyRequest = createRequestWithViolations(mock(Violation.class), mock(Violation.class));
		when(moneyRequestBundle.getRequests()).thenReturn(ImmutableList.of(firstMoneyRequest, secondMoneyRequest, thirdMoneyRequest));

		Collection<Violation> returnedViolations = rule.isSatisfied(moneyRequestBundle);

		assertNotNull("Returned violations cannot be null.", returnedViolations);
		assertFalse("Returned violations cannot be empty.", returnedViolations.isEmpty());
		assertEquals("It must return 1 violations", 1, returnedViolations.size());
	}

	private MoneyRequest createRequestWithViolations(Violation... expectedViolations) {
		MoneyRequest moneyRequest = mock(MoneyRequest.class);
		when(businessRulesValidator.validate(moneyRequest)).thenReturn(Arrays.asList(expectedViolations));
		return moneyRequest;
	}

	@InjectMocks
	private ChildrenMoneyRequestsAreValid rule;

	@Mock
	private MoneyRequestBundle moneyRequestBundle;

	@Mock
	private BusinessRulesValidator<MoneyRequest> businessRulesValidator;

}