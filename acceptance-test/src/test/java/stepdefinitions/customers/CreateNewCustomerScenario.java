package stepdefinitions.customers;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.ari.CucumberContext;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

@ContextConfiguration("classpath:cucumber.xml")
public class CreateNewCustomerScenario {

	@When("^i try to create a new customer with with code \"(.*?)\"$")
	public void i_try_to_create_a_new_customer_with_with_code(String customerCode) {
		Response response = requestSender.post("customers", customersFactory.createCustomer(customerCode));

		cucumberContext.publishValue("response", response);
		cucumberContext.publishValue("customerCode", customerCode);
	}

	@Then("^the response have the identifier of the customer$")
	public void the_response_have_the_identifier_of_the_customer() {
		Response response = (Response) cucumberContext.getValue("response");
		Map<String, Object> entity = restJsonReader.read(response);

		assertNotNull("The read entity must be not null", entity);
		assertNotNull("The entity id must be not null", entity.get("entityId"));

		String customerId = (String) entity.get("entityId");
		customersRegistry.registerCustomer(customerId, (String) cucumberContext.getValue("customerCode"));
		cucumberContext.publishValue("customerId", customerId);
	}

	@Autowired
	private RestClient requestSender;

	@Autowired
	private CustomersRegistry customersRegistry;

	@Autowired
	private CustomersFactory customersFactory;

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private RestJsonReader restJsonReader;

}
