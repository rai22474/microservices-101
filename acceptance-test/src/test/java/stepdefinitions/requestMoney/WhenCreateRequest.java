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
import java.util.function.Supplier;

@ContextConfiguration("classpath:cucumber.xml")
public class WhenCreateRequest {

	@When("^customer \"(.*?)\" requests \"(.*?)\" to other customer with identifier \"(.*?)\" with reason \"(.*?)\"$")
	public void customer_requests_to_other_customer_with_identifier_with_reason(String senderIdCard, String amount, String recipientIdCard, String reason) {
		createMoneyRequest(senderIdCard, () -> moneyRequestsFactory.createMoneyRequests(recipientIdCard, amount, reason));
	}

	@When("^customer \"(.*?)\" creates a money request, caused by \"(.*?)\", with the following data:$")
	public void customer_creates_a_money_request_caused_by_with_the_following_data(String senderIdCard, String reason, List<Map<String, Object>> requestsData) {
		createMoneyRequest(senderIdCard, () -> moneyRequestsFactory.createMoneyRequests(reason, requestsData));
	}

	@When("^customer \"(.*?)\" requests \"(.*?)\" to other customer with identifier \"(.*?)\" with no reason$")
	public void customer_requests_to_other_customer_with_identifier_with_no_reason(String senderCard, String amount, String recipientIdCard) {
		createMoneyRequest(senderCard, () -> moneyRequestsFactory.createMoneyRequests(recipientIdCard, amount));
	}

	@When("^customer \"(.*?)\" creates a money request with the following properties$")
	public void customerCreatesAMoneyRequestsWithTheFollowingProperties(String senderIdCard, List<Map<String, Object>> ordersData) {
		createMoneyRequest(senderIdCard, () -> moneyRequestsFactory.createDefaultBundle(ordersData));
	}

	private void createMoneyRequest(String senderIdCard, Supplier<Map<String, Object>> moneyRequestDataSupplier) {
		String customerId = customersRegistry.getCustomerId(senderIdCard);

		Response response = writeClient.post(
				"moneyRequests",
				moneyRequestDataSupplier.get(),
				ImmutableMap.of("x-customer-id", customerId));

		cucumberContext.publishValue("response", response);
		cucumberContext.publishValue("responseEntity", restJsonReader.read(response));
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
