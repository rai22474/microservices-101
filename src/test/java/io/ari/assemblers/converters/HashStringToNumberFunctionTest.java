package io.ari.assemblers.converters;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HashStringToNumberFunctionTest {


	@Test
	public void shouldHashAnStringAnTranformToANumber() {
		Integer convertedValue = hashStringToNumberFunction.apply("132413dsdfasdfa");
		assertEquals("The value of the amount must be 204", new Integer("276"), convertedValue);
	}

	@Test
	public void shouldHashAnStringAnDontReturnNegativeNumbers() {
		Integer convertedValue = hashStringToNumberFunction.apply("1");
		assertEquals("The value of the amount must be 204", new Integer("736"), convertedValue);
	}

	@Test
	public void shouldReturnTheNumberIfIsLessOfUpperLimit() {
		Number convertedValue = hashStringToNumberFunction.adaptNumberToUpperLimit(3);
		assertEquals("The value of the amount must be 3", new Integer(3), convertedValue);
	}

	@Test
	public void shouldReturnDivideByUpperLimit() {
		Number convertedValue = hashStringToNumberFunction.adaptNumberToUpperLimit(1001);
		assertEquals("The value of the amount must be 1", new Integer(1), convertedValue);
	}

	private HashStringToNumberFunction hashStringToNumberFunction = new HashStringToNumberFunction();
}
