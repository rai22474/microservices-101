package stepdefinitions.requestMoney;

import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenRequestDraftHasData {

	@Then("^the money request draft has the following data:$")
	public void the_money_request_draft_has_the_following_data(List<Map<String, Object>> expected_draft_data) {
		checker.verifyRequest(expected_draft_data);
	}

	@Autowired
	private MoneyRequestsChecker checker;

}
