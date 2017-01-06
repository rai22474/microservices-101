package stepdefinitions.requestMoney;

import cucumber.api.java.en.Then;
import io.ari.CucumberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.commands.CommandsRegistry;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenRequestHasErrors {

	@Then("^the returned money request bundle must have the following errors$")
	public void the_returned_money_request_bundle_must_have_the_following_errors(List<Map<String, Object>> expectedErrors) {
		Map<String, Object> bundleResponse = (Map<String, Object>) cucumberContext.getValue("responseEntity");
		commandsRegistry.add((String) bundleResponse.get("id"));

		assertTrue("Returned request bundle must have a status item.", bundleResponse.containsKey("status"));
		Collection<Map<String, Object>> bundleErrors = (Collection<Map<String, Object>>) bundleResponse.get("status");
		expectedErrors.forEach(expectedError -> verifyErrorExists(bundleErrors, expectedError));
	}

	@Then("^the returned money request must have the following errors$")
	public void the_returned_money_request_must_have_the_following_errors(List<Map<String, Object>> expectedErrors) {
		Map<String, Object> bundleResponse = (Map<String, Object>) cucumberContext.getValue("responseEntity");
		commandsRegistry.add((String) bundleResponse.get("id"));

		Collection<Map<String, Object>> moneyRequests =  (Collection<Map<String, Object>>) bundleResponse.get("moneyRequests");
		Map<String, Object> moneyRequest = moneyRequests.stream().findFirst().get();

		assertTrue("The response must have property moneyRequests.status", moneyRequest.containsKey("status"));
		Collection<Map<String, Object>> errors =  (Collection<Map<String, Object>>) moneyRequest.get("status");
		expectedErrors.forEach(expectedError -> verifyErrorExists(errors, expectedError));
	}

	private void verifyErrorExists(Collection<Map<String, Object>> errors, Map<String, Object> expectedError) {
		List<Map<String, Object>> matchingErrors = errors
				.stream()
				.filter(error -> expectedError.get("code").equals(error.get("code")))
				.filter(error -> expectedError.get("description").equals(expectedError.get("description")))
				.collect(Collectors.toList());

		assertFalse("There must exist the error " + expectedError.get("code") + " in the money order bundle.", matchingErrors.isEmpty());
	}

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private CommandsRegistry commandsRegistry;

}
