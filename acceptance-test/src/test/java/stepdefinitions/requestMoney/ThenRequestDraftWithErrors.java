package stepdefinitions.requestMoney;

import cucumber.api.java.en.Then;
import io.ari.CucumberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.commands.CommandsRegistry;

import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenRequestDraftWithErrors {

	@Then("^the money request draft have some errors$")
	public void the_money_request_draft_have_some_errors() {
		Map<String, Object> moneyRequestDraft = (Map<String, Object>) cucumberContext.getValue("responseEntity");

		assertNotNull("The money request draft must be not null", moneyRequestDraft);
		assertNotNull("The money request draft must have id", moneyRequestDraft.get("id"));
		commandsRegistry.add((String) moneyRequestDraft.get("id"));

		assertTrue("The money request draft must contain errors.", moneyRequestDraft.containsKey("status"));
	}

	@Autowired
	private CommandsRegistry commandsRegistry;

	@Autowired
	private CucumberContext cucumberContext;

}
