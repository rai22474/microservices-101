package stepdefinitions.requestMoney;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import stepdefinitions.commands.CommandsRegistry;
import cucumber.api.java.After;

@ContextConfiguration("classpath:cucumber.xml")
public class DeleteMoneyRequestsHook {

	@After(value = "@deleteMoneyRequestBundle")
	public void deleteMoneyRequests() {
		commandsRegistry.deleteAll();
	}

	@Autowired
	private CommandsRegistry commandsRegistry;

}
