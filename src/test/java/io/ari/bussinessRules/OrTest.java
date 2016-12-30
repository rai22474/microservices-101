package io.ari.bussinessRules;

import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrTest {

	@Test
	public void shouldNotReturnNull() {
		Collection<BusinessRule<Object>> rules = ImmutableSet.of();
		or = new Or<Object>(rules, CODE, MESSAGE);

		assertNotNull(or.isSatisfied(objectToValidate));
	}

	@Test
	public void shouldReturnViolationsWithAllFalseBusinessRules(){
		BusinessRule<Object> ruleError = mock(BusinessRule.class);
		when(ruleError.isSatisfied(objectToValidate)).thenReturn(ImmutableSet.of(violation));
		or = new Or<Object>(ImmutableSet.of(ruleError,ruleError), CODE, MESSAGE);
		assertTrue("Should return violations", or.isSatisfied(objectToValidate).isEmpty() == false);
	}


	@Test
	public void shouldReturnEmptyViolationsWithAnyCorrectBusinessRule(){
		BusinessRule<Object> ruleError = mock(BusinessRule.class);
		BusinessRule<Object> ruleOk = mock(BusinessRule.class);
		when(ruleOk.isSatisfied(objectToValidate)).thenReturn(ImmutableSet.of(violation));
		when(ruleError.isSatisfied(objectToValidate)).thenReturn(ImmutableSet.of());
		or = new Or<Object>(ImmutableSet.of(ruleError,ruleOk), CODE, MESSAGE);

		assertTrue("Should return empty violations", or.isSatisfied(objectToValidate).isEmpty());
	}

	@Test
	public void shouldReturnTrueWithAllTrueBusinessRules(){
		BusinessRule<Object> ruleOk = mock(BusinessRule.class);
		BusinessRule<Object> ruleOk2 = mock(BusinessRule.class);
		when(ruleOk.isSatisfied(objectToValidate)).thenReturn(ImmutableSet.of());
		when(ruleOk2.isSatisfied(objectToValidate)).thenReturn(ImmutableSet.of());
		or = new Or<Object>(ImmutableSet.of(ruleOk,ruleOk2), CODE, MESSAGE);

		assertTrue("Should not return violations", or.isSatisfied(objectToValidate).isEmpty());
	}


	@Mock
	private Object objectToValidate;

	@Mock
	private Violation violation;

	private Or<Object> or;

	private final static String CODE = "OrCode";
	private final static String MESSAGE = "OrMessage";
}
