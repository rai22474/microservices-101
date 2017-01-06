package stepdefinitions.requestMoney;

import com.google.common.collect.ImmutableMap;
import cucumber.api.java.en.When;
import io.ari.CucumberContext;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.customers.CustomersRegistry;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@ContextConfiguration("classpath:cucumber.xml")
public class WhenEditRequestDraft {

	@When("^the customer \"(.*?)\" updates the money request draft, requesting \"(.*?)\" to the customer \"(.*?)\" with reason \"(.*?)\"$")
	public void the_customer_updates_the_money_request_draft_sending_to_the_wizzer_with_reason(String senderIdCard, String amount, String recipientIdCard, String reason) {

		Map<String, Object> newDraftData = moneyRequestsFactory.createMoneyRequests(recipientIdCard, amount, reason);
		whenEditRequestDraft(senderIdCard, newDraftData);

	}

	@When("^customer \"(.*?)\" edit a money request draft with the following properties$")
	public void customerEditAMoneyRequestsDraftWithTheFollowingProperties(String senderIdCard, List<Map<String, Object>> ordersData) {

		whenEditRequestDraft(senderIdCard, moneyRequestsFactory.createDefaultBundle(ordersData));

	}

	private void whenEditRequestDraft(String senderIdCard, Map<String, Object> requestMoneyDraft) {

		String draftId = (String) cucumberContext.getValue("moneyRequestDraftId");

		Response response = restClient.put(
				"drafts/moneyRequests/" + draftId,
				requestMoneyDraft,
				ImmutableMap.of("x-customer-id", customersRegistry.getCustomerId(senderIdCard)));

		cucumberContext.publishValue("response", response);
		cucumberContext.publishValue("responseEntity", restJsonReader.read(response));
	}

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private RestClient restClient;

	@Autowired
	private MoneyRequestsFactory moneyRequestsFactory;

	@Autowired
	private RestJsonReader restJsonReader;

	@Autowired
	private CustomersRegistry customersRegistry;

}
