package stepdefinitions.orderMoney;

import com.google.common.collect.ImmutableMap;
import cucumber.api.java.en.When;
import io.ari.CucumberContext;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.customers.CustomersRegistry;

import javax.ws.rs.core.Response;

@ContextConfiguration("classpath:cucumber.xml")
public class WhenEditOrderDraft {

	@When("^the customer \"([^\"]*)\" updates the money order draft, sending \"([^\"]*)\" to the customer \"([^\"]*)\" with reason \"([^\"]*)\"$")
	public void the_customer_updates_the_money_order_draft_sending_to_the_customer(String senderIdCard, String newAmount, String recipientIdCard, String newReason) {

		Response moneyOrderDraftResponse = restClient.put(
				"drafts/moneyOrders/" + cucumberContext.getValue("moneyOrderDraftId"),
				moneyOrderFactory.createSingleMoneyOrderBundle(recipientIdCard, newAmount, newReason),
				ImmutableMap.of("x-customer-id", customersRegistry.getCustomerId(senderIdCard)));

		cucumberContext.publishValue("response", moneyOrderDraftResponse);
		cucumberContext.publishValue("responseEntity", restJsonReader.read(moneyOrderDraftResponse));
	}

	@Autowired
	private RestClient restClient;

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private MoneyOrdersFactory moneyOrderFactory;

	@Autowired
	private RestJsonReader restJsonReader;

	@Autowired
	private CustomersRegistry customersRegistry;

}
