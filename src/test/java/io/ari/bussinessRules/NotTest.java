package io.ari.bussinessRules;


import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class NotTest {

	@Test
	public void shouldNotReturnNull() {
		assertNotNull(not.isSatisfied(objectToValidate));
	}

	@Test
	public void shouldReturnEmptyViolationWhenRuleIsNotSatisfied() {
		Violation violation = mock(Violation.class);

		when(rule.isSatisfied(objectToValidate)).thenReturn(ImmutableList.of(violation));

		assertTrue("Should return no violations", not.isSatisfied(objectToValidate).isEmpty());
	}

	@Test
	public void shouldReturnViolationWhenRuleIsSatisfied() {
		when(rule.isSatisfied(objectToValidate)).thenReturn(ImmutableList.of());

		assertFalse("Should return violations",not.isSatisfied(objectToValidate).isEmpty());
	}

	@Test
	public void shouldReturnCodeAndDescription(){

		when(rule.isSatisfied(objectToValidate)).thenReturn(ImmutableList.of());

		Collection<Violation> violations = not.isSatisfied(objectToValidate);
		assertEquals("Must have one violation", violations.size(), 1);
		Violation returnedViolation = violations.iterator().next();
		assertEquals("Code must be the provided", returnedViolation.getCode(), CODE);
		assertEquals("Message must be the provided", returnedViolation.getMessage(), MESSAGE);
	}

	@Before
	public void setup() {
		not = new Not<Object>(rule, CODE, MESSAGE);
	}

	@Mock
	private BusinessRule<Object> rule;

	@Mock
	private Object objectToValidate;

	private Not<Object> not;

	private final static String CODE = "NotCode";
	private final static String MESSAGE = "NotMessage";
}
