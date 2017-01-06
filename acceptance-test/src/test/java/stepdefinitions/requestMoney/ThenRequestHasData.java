package stepdefinitions.requestMoney;


import java.util.List;
import java.util.Map;

import io.ari.CucumberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import stepdefinitions.commands.CommandsRegistry;


import cucumber.api.java.en.Then;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenRequestHasData {

	@Then("^the money request has the following data:$")
	public void the_money_request_has_the_following_data(List<Map<String, Object>> expectedRequestData) {
		Map<String, Object> moneyRequestBundle = (Map<String, Object>) cucumberContext.getValue("responseEntity");
		String commandId = (String) moneyRequestBundle.get("id");
		commandsRegistry.add(commandId);
		((List<Map<String, Object>>) moneyRequestBundle.get("moneyRequests"))
				.forEach(moneyRequest -> commandsRegistry.add((String) moneyRequest.get("id")));

		checker.verifyRequest(expectedRequestData);
	}

	@Autowired
	private MoneyRequestsChecker checker;

	@Autowired
	private CommandsRegistry commandsRegistry;

	@Autowired
	private CucumberContext cucumberContext;

}
