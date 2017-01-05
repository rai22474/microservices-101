package stepdefinitions.orderMoney;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.ari.CucumberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


import cucumber.api.java.en.Then;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenOrderHasRecipientData {

	@Then("^the money order has a recipient with the following data:$")
	public void the_money_order_has_a_recipient_with_the_following_data(List<Map<String, String>> expected_money_order_data) {
		Map<String, Object> moneyOrderBundle = (Map<String, Object>) cucumberContext.getValue("responseEntity");
		List<Map<String, Object>> moneyOrders = (List<Map<String, Object>>) moneyOrderBundle.get("moneyOrders");
		Iterator<Map<String, Object>> moneyOrdersIterator = moneyOrders.iterator();

		expected_money_order_data.forEach(expectedMoneyOrderData -> {
			Map<String, Object> moneyOrder = moneyOrdersIterator.next();
			expectedMoneyOrderData.forEach((key, value) -> {
				assertTrue("The money order must have a " + key, moneyOrder.containsKey(key));
				assertEquals("The money order " + key + " must be the expected", value, moneyOrder.get(key));
			});
		});
	}

	@Autowired
	private CucumberContext cucumberContext;

}
