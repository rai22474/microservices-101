package stepdefinitions.requestMoney;

import com.google.common.collect.ImmutableMap;
import cucumber.api.java.en.Given;
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

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:cucumber.xml")
public class WhenCreateRequestDraft {

	@When("^customer \"(.*?)\" creates a request draft with \"(.*?)\" to other customer with identifier \"(.*?)\" with reason \"(.*?)\"$")
	public void customer_creates_a_request_draft_with_to_other_customer_with_identifier_with_reason(String senderIdCard, String amount, String recipientIdCard, String reason) {
		whenCreateRequestDraft(senderIdCard, moneyRequestsFactory.createMoneyRequests(recipientIdCard, amount, reason));
	}

	@When("^customer \"(.*?)\" creates an request draft with reason \"(.*?)\" with the following recipients$")
	public void customer_creates_an_request_draft_with_reason_with_the_following_recipients(String senderIdCard, String reason, List<Map<String, Object>> recipients) {
		whenCreateRequestDraft(senderIdCard, moneyRequestsFactory.createMoneyRequests(reason, recipients));
	}

	@Given("^a request draft with \"(.*?)\" to other customer with identifier \"(.*?)\" with reason \"(.*?)\", created by the customer \"(.*?)\"$")
	public void a_request_draft_with_to_other_customer_with_identifier_with_reason_created_by_the_customer(String amount, String recipientIdCard, String reason, String senderIdCard) {
		Response response = createRequestDraft(senderIdCard, moneyRequestsFactory.createMoneyRequests(recipientIdCard, amount, reason));
		assertEquals("Money request draft POST must return CREATED.", 201, response.getStatus());

		cucumberContext.publishValue("moneyRequestDraftId", restJsonReader.read(response).get("id"));
	}

	@When("^customer \"(.*?)\" creates a money request draft with the following properties$")
	public void customerCreatesAMoneyRequestsDraftWithTheFollowingProperties(String senderIdCard, List<Map<String, Object>> ordersData) {
		whenCreateRequestDraft(senderIdCard, moneyRequestsFactory.createDefaultBundle(ordersData));
	}

	private void whenCreateRequestDraft(String senderIdCard, Map<String, Object> moneyRequests) {
		Response response = createRequestDraft(senderIdCard, moneyRequests);

		cucumberContext.publishValue("response", response);
		cucumberContext.publishValue("responseEntity", restJsonReader.read(response));
	}

	private Response createRequestDraft(String senderIdCard, Map<String, Object> moneyRequests) {
		return writeClient.post(
				"drafts/moneyRequests",
				moneyRequests,
				ImmutableMap.of("x-customer-id", customersRegistry.getCustomerId(senderIdCard)));
	}

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private RestClient writeClient;

	@Autowired
	private MoneyRequestsFactory moneyRequestsFactory;

	@Autowired
	private RestJsonReader restJsonReader;

	@Autowired
	private CustomersRegistry customersRegistry;

}
