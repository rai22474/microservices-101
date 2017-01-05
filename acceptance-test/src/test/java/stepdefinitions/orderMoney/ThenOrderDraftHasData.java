package stepdefinitions.orderMoney;

import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenOrderDraftHasData {

	@Then("^the money order draft has the following data:$")
	public void the_draft_has_the_following_data(List<Map<String, String>> expectedBundles) {
		moneyOrderChecker.verifyMoneyOrderBundle(expectedBundles);
	}

	@Autowired
	private MoneyOrdersChecker moneyOrderChecker;

}
