package io.ari.bussinessRules;

import com.google.common.collect.ImmutableSet;
import io.ari.money.domain.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;

import static io.ari.money.MoneyBuilder.val;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BusinessRulesValidatorTest {

	@Test
	public void shouldNotReturnNull() {
		validator.setRules(ImmutableSet.of());

		Collection<Violation> violations = validator.validate(domainEntity, money);

		assertNotNull("Returned violations cannot be null.", violations);
	}

	@Test
	public void shouldReturnNoViolation() {
		Collection<Violation> violations = validator.validate(domainEntity, money);

		assertTrue("It must be returned no violations.", violations.isEmpty());
	}

	@Test
	public void shouldReturnAgreggationOfViolationsFromRules() {
		BusinessRule<DomainEntity> rule1 = createFailingRuleFor(domainEntity, money, mock(Violation.class));
		BusinessRule<DomainEntity> rule2 = createSuccessRuleFor(domainEntity, money);
		BusinessRule<DomainEntity> rule3 = createFailingRuleFor(domainEntity, money, mock(Violation.class), mock(Violation.class));
		validator.setRules(ImmutableSet.of(rule1, rule2, rule3));

		Collection<Violation> violations = validator.validate(domainEntity, money);

		assertFalse("It must be returned some violations.", violations.isEmpty());
		assertEquals("It must be returned 3 violations.", 3, violations.size());
	}

	private BusinessRule<DomainEntity> createSuccessRuleFor(DomainEntity domainEntity, Money money) {
		BusinessRule<DomainEntity> rule = mock(BusinessRule.class);
		when(rule.isSatisfied(domainEntity, money)).thenReturn(ImmutableSet.of());
		return rule;
	}

	private BusinessRule<DomainEntity> createFailingRuleFor(DomainEntity domainEntity, Money money, Violation... violations) {
		BusinessRule<DomainEntity> rule = mock(BusinessRule.class);
		when(rule.isSatisfied(domainEntity, money)).thenReturn(Arrays.asList(violations));
		return rule;
	}

	@InjectMocks
	private BusinessRulesValidator<DomainEntity> validator = new BusinessRulesValidator<>();

	@Mock
	private DomainEntity domainEntity;

	private Money money = val("10").eur().entity();

}

class DomainEntity {
}