package io.ari.money.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.math.BigDecimal;


import static io.ari.money.MoneyBuilder.val;
import static org.junit.Assert.*;

public class MoneyTest {

	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionWhenComparingWithDifferentCurrencies() {
		greaterMoney.isEnoughFor(dollarsMoney);
	}

	@Test
	public void shouldBeEnoughFor() {
		assertTrue("Money should by enough for the given one", greaterMoney.isEnoughFor(lesserMoney));
	}

	@Test
	public void shouldNotBeEnoughFor() {
		assertFalse("Money should by enough for the given one", lesserMoney.isEnoughFor(greaterMoney));
	}

	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionWhenAddWithDifferentCurrencies() {
		greaterMoney.add(dollarsMoney);
	}
	
	@Test
	public void shouldAddMoney(){
		Money result = greaterMoney.add(lesserMoney);
		
		assertNotNull("The result must not be null",result);
		assertEquals("The money is not the expected",val("151.00").eur().entity(),result);
	}
	
	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionWhenSubtractWithDifferentCurrencies() {
		greaterMoney.subtract(dollarsMoney);
	}
	
	@Test
	public void shouldSubtractMoney(){
		Money result = greaterMoney.subtract(lesserMoney);
		
		assertNotNull("The result must not be null",result);
		assertEquals("The money is not the expected",val("50.00").eur().entity(),result);
	}
	
	@Test
	public void shouldNegateMoneyValue(){
		Money result = val("151.00").eur().entity().negate();
		
		assertNotNull("The result must not be null",result);
		assertEquals("The money is not the expected",val("-151.00").eur().entity(),result);
	}
	
	@Test
	public void shouldNegateNegativeMoneyValue(){
		Money result = val("-151.00").eur().entity().negate();
		
		assertNotNull("The result must not be null",result);
		assertEquals("The money is not the expected",val("151.00").eur().entity(),result);
	}
	
	@Test
	public void shouldBeEquals(){
		EqualsVerifier.forClass(Money.class)
		.usingGetClass()
		.verify();
	}
	
	private Money greaterMoney = new Money(new BigDecimal("100.50"), "EUR");
	
	private Money lesserMoney = new Money(new BigDecimal("50.50"), "EUR");
	
	private Money dollarsMoney = new Money(new BigDecimal("50"), "DOL");
}
