package stepdefinitions.requestMoney;


import cucumber.api.java.en.Then;
import io.ari.CucumberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.commands.CommandsRegistry;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenRequestDraftWithoutErrors {

	@Then("^the money request draft doesn't have any errors$")
	public void the_money_request_draft_doesn_t_have_any_errors() {
		Map<String, Object> moneyRequestDraft = (Map<String, Object>) cucumberContext.getValue("responseEntity");

		assertNotNull("The money request draft must be not null", moneyRequestDraft);
		assertNotNull("The money request draft must have id", moneyRequestDraft.get("id"));
		commandsRegistry.add((String) moneyRequestDraft.get("id"));

		assertFalse("The money request draft cannot contain errors.", moneyRequestDraft.containsKey("status"));
		Collection<Map<String, Object>> moneyRequests = (Collection<Map<String, Object>>) moneyRequestDraft.get("moneyRequests");
		moneyRequests.forEach(moneyRequest ->
				assertFalse("The single money request cannot contain errors.", moneyRequest.containsKey("status")));
	}

	@Autowired
	private CommandsRegistry commandsRegistry;

	@Autowired
	private CucumberContext cucumberContext;

}
