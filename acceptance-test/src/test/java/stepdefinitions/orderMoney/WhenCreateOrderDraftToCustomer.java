package stepdefinitions.orderMoney;

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
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:cucumber.xml")
public class WhenCreateOrderDraftToCustomer {


	@When("^customer \"(.*?)\" create a order draft with \"(.*?)\" to other customer with identifier \"(.*?)\" with reason \"(.*?)\"$")
	public void customer_create_a_order_draft_with_to_other_customer_with_identifier_with_reason(String senderIdCard, String amount, String recipientIdCard, String reason) {
		createMoneyOrderDraft(senderIdCard ,() -> moneyOrderFactory.createSingleMoneyOrderBundle(recipientIdCard, amount, reason));
	}

	@Given("^an order draft with \"([^\"]*)\" to other customer with identifier \"([^\"]*)\" with reason \"([^\"]*)\", created by the customer \"([^\"]*)\"$")
	public void an_order_draft_with_to_other_customer_with_identifier_with_reason_created_by_the_customer(String amount, String recipientIdCard, String reason, String senderIdCard) {
		Response moneyOrderDraftResponse = createMoneyOrderDraft(senderIdCard, () -> moneyOrderFactory.createSingleMoneyOrderBundle(recipientIdCard, amount, reason));
		assertEquals("Money order draft POST must return a 201.", 201, moneyOrderDraftResponse.getStatus());

	}

    @When("^customer \"([^\"]*)\" creates a money order draft with the following properties$")
    public void customer_creates_a_money_order_draft_with_the_following_properties(String customerIdCard, List<Map<String, Object>> ordersData) throws Throwable {
        createMoneyOrderDraft(customerIdCard, () -> moneyOrderFactory.createDefaultBundle(ordersData));
    }


	private Response createMoneyOrderDraft(String senderIdCard, Supplier<Map<String, Object>> moneyOrdersSupplier) {

        Response moneyOrderDraftResponse = restClient.post(
				"drafts/moneyOrders",
                moneyOrdersSupplier.get(),
				ImmutableMap.of("x-customer-id", customersRegistry.getCustomerId(senderIdCard))
        );

        cucumberContext.publishValue("response", moneyOrderDraftResponse);

        Map<String, Object> returnedDraft = restJsonReader.read(moneyOrderDraftResponse);
        cucumberContext.publishValue("responseEntity", returnedDraft);
        cucumberContext.publishValue("moneyOrderDraftId", returnedDraft.get("id"));

        return moneyOrderDraftResponse;
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
