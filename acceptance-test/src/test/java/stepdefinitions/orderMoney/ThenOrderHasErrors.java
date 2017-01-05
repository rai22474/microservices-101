package stepdefinitions.orderMoney;

import cucumber.api.java.en.Then;
import io.ari.CucumberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.commands.CommandsRegistry;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenOrderHasErrors {

	@Then("^the returned money order bundle must have the following errors$")
	public void the_returned_money_order_bundle_must_have_the_following_errors(List<Map<String, Object>> expectedErrors) {
		Map<String, Object> bundleResponse = (Map<String, Object>) cucumberContext.getValue("responseEntity");
		commandsRegistry.add((String) bundleResponse.get("id"));
		assertTrue("The response must have property moneyOrders.status", bundleResponse.containsKey("status"));
		Collection<Map<String, Object>> bundleErrors = (Collection<Map<String, Object>>) bundleResponse.get("status");
		expectedErrors.forEach(expectedError -> verifyBundleError(bundleErrors, expectedError));
	}

	@Then("^the returned money order must have the following errors$")
	public void the_returned_money_order_must_have_the_following_errors(List<Map<String, Object>> expectedErrors)  {
		Map<String, Object> bundleResponse = (Map<String, Object>) cucumberContext.getValue("responseEntity");
		commandsRegistry.add((String) bundleResponse.get("id"));

		Collection<Map<String, Object>> moneyOrders =  (Collection<Map<String, Object>>) bundleResponse.get("moneyOrders");
		Map<String, Object> moneyOrder = moneyOrders.stream().findFirst().get();

		assertTrue("The response must have property moneyOrders.status", moneyOrder.containsKey("status"));
		Collection<Map<String, Object>> errors =  (Collection<Map<String, Object>>) moneyOrder.get("status");
		expectedErrors.forEach(expectedError -> verifyBundleError(errors, expectedError));
	}

	private void verifyBundleError(Collection<Map<String, Object>> bundleErrors, Map<String, Object> expectedError) {
		List<Map<String, Object>> matchingErrors = bundleErrors
				.stream()
				.filter(error -> expectedError.get("code").equals(error.get("code")))
				.filter(error -> expectedError.get("description").equals(expectedError.get("description")))
				.collect(toList());

		assertFalse("There must exist the error " + expectedError.get("code") + " in the money order bundle.", matchingErrors.isEmpty());
	}

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private CommandsRegistry commandsRegistry;
}
