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
public class ThenCustomerHasData {

	@Then("^therefore, it exists a customer, identified by \"(.*?)\" with the following data:$")
	public void therefore_it_exists_a_customer_identified_by_x_with_the_following_data(String customerId, List<Map<String, Object>> expectedData) {

		Map<String, Object> headers = new HashMap<>();
		headers.put("x-customer-id", customerId);
		Response response = requestSender.get("me", headers);

		assertEquals("Customer " + customerId + " must exist.", 200, response.getStatus());

		Map<String, Object> expectedCustomer = expectedData.stream().findFirst().get();
		Map<String, Object> customer = restJsonReader.read(response);

		expectedCustomer
				.forEach((key, value) -> {
					assertTrue("The customer must have a " + key, customer.containsKey(key));
					assertEquals("The customer " + key + " must be the expected.", value, customer.get(key));
				});
	}

	@Autowired
	private RestClient requestSender;

	@Autowired
	private RestJsonReader restJsonReader;

}
