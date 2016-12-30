package stepdefinitions.customers;

import cucumber.api.java.en.Then;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenCustomerHasUpdatedData {

	@Then("^the customer \"([^\"]*)\" has his address updated to$")
	public void the_customer_has_his_address_updated_to(String customerIdCard, List<Map<String, Object>> expectedData) {
		Map<String, Object> expectedAddress = expectedData.stream().findFirst().get();

		String customerId = customersRegistry.getCustomerId(customerIdCard);
		Map<String, Object> headers = new HashMap<>();
		headers.put("x-customer-id", customerId);

		Response response = requestSender.get("me", headers);
		Map<String, Object> customer = restJsonReader.read(response);
		Map<String, Object> address = (Map<String, Object>) customer.get("address");

		address.forEach((key, value) -> {
			assertTrue("The address must have a " + key, expectedAddress.containsKey(key));
			assertEquals("The address " + key + " must be the expected.", value, expectedAddress.get(key));
		});

	}

	@Then("^the customer \"([^\"]*)\" has his name and last name updated to \"([^\"]*)\" \"([^\"]*)\"$")
	public void the_customer_has_his_name_and_last_name_updated_to(String customerIdCard, String name, String lastName) {

		String customerId = customersRegistry.getCustomerId(customerIdCard);
		Map<String, Object> headers = new HashMap<>();
		headers.put("x-customer-id", customerId);

		Response response = requestSender.get("me", headers);

		Map<String, Object> customer = restJsonReader.read(response);

		assertEquals("The customer name is updated", name, customer.get("name"));
		assertEquals("The customer last name is updated", lastName, customer.get("lastName"));
	}

	@Then("^the customer \"([^\"]*)\" has his email updated to \"([^\"]*)\"$")
	public void the_customer_has_his_email_updated_to(String customerIdCard, String email) {

		String customerId = customersRegistry.getCustomerId(customerIdCard);
		Map<String, Object> headers = new HashMap<>();
		headers.put("x-customer-id", customerId);

		Response response = requestSender.get("me", headers);

		Map<String, Object> customer = restJsonReader.read(response);

		assertEquals("The customer email is updated", email, customer.get("email"));
	}

	@Autowired
	private RestClient requestSender;

	@Autowired
	private RestJsonReader restJsonReader;

	@Autowired
	private CustomersRegistry customersRegistry;
}
