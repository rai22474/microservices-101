package stepdefinitions.orderMoney;

import cucumber.api.java.en.Then;
import io.ari.CucumberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.commands.CommandsRegistry;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenOrderDraftWithoutErrors {

	@Then("^there are a money order draft without errors$")
	public void there_are_a_money_order_draft_without_errors() {
		Map<String, Object> moneyOrderDraft = (Map<String, Object>) cucumberContext.getValue("responseEntity");

		assertNotNull("The money order draft must be not null", moneyOrderDraft);
		String moneyOrderBundleId = (String) moneyOrderDraft.get("id");

		assertNotNull("The money order must have id", moneyOrderBundleId);
		commandsRegistry.add(moneyOrderBundleId);
	}

	@Autowired
	private CommandsRegistry commandsRegistry;

	@Autowired
	private CucumberContext cucumberContext;

}
