package stepdefinitions.customers;

import io.ari.CucumberContext;
import io.ari.RestClient;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:cucumber.xml")
public class GivenAnExistingCustomer {

	@Given("^a customer with identity card \"(.*?)\"$")
	public void a_customer_with_identity_card(String identityCard) {
		createNewCustumer(identityCard);
	}

	@After(value = "@deleteCustomer", order = 10)
	public void deleteRegisterCustomers() {
		deleteCustomersHook.getCustomersIdsAsArrays().forEach(deleteCustomersHook::deleteCustomers);
	}

	private void createNewCustumer(String identityCard) {
		Response response = dataLoaderClient.post("io/ari/subjects", customersFactory.createCustomer(identityCard));
		assertEquals("The response status must be created", 201, response.getStatus());

		Map<String, Object> entity = response.readEntity((new GenericType<Map<String, Object>>() {
		}));
		String customerId = (String) entity.get("entityId");

		deleteCustomersHook.registerCustomers(customerId);
		cucumberContext.publishValue("clientCode", customerId);
		cucumberContext.publishValue("customerId", customerId);

		Map<String, String> customerIdentifiers = new HashMap<>();
		customerIdentifiers.put(identityCard, customerId);

		cucumberContext.publishValue("customerIdentifiers", customerIdentifiers);
	}

	@Autowired
	private RestClient dataLoaderClient;

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private DeleteCustomersHook deleteCustomersHook;

	@Autowired
	private CustomersFactory customersFactory;
}
