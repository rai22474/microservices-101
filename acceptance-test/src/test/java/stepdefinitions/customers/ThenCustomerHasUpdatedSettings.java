package stepdefinitions.customers;


import cucumber.api.java.en.And;
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

public class ThenCustomerHasUpdatedSettings {

	@And("^the customer \"([^\"]*)\" has his notifications updated to$")
	public void the_customer_has_his_settings_updated_to(String customerIdCard, List<Map<String, Object>> expectedData) {
		Map<String, Object> expectedSettings = expectedData.stream().findFirst().get();

		String customerId = customersRegistry.getCustomerId(customerIdCard);
		Map<String, Object> headers = new HashMap<>();
		headers.put("x-customer-id", customerId);

		Response response = requestSender.get("me", headers);

		Map<String, Object> customer = restJsonReader.read(response);

		Map<String, Object> settings = (Map<String, Object>) customer.get("settings");

		assertTrue("The settings must have a notifications map", settings.containsKey("notifications"));

		Map<String, Object> returnedSettings = (Map<String, Object>) customer.get("settings");
		Map<String, Object> notifications = (Map<String, Object>) returnedSettings.get("notifications");

		notifications.forEach((key, value) -> {
			assertTrue("The settings must have a " + key, expectedSettings.containsKey(key));
			assertEquals("The settings " + key + " must be the expected.", value, expectedSettings.get(key));
		});
	}

	@Autowired
	private RestClient requestSender;

	@Autowired
	private RestJsonReader restJsonReader;

	@Autowired
	private CustomersRegistry customersRegistry;
}
