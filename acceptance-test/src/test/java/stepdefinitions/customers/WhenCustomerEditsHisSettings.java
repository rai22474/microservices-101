package stepdefinitions.customers;

import cucumber.api.java.en.When;
import io.ari.CucumberContext;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@ContextConfiguration("classpath:cucumber.xml")
public class WhenCustomerEditsHisSettings {
	@When("^the customer \"([^\"]*)\" updates his notifications to:$")
	public void the_customer_updates_his_settings_to(String customerIdCard, List<Map<String, Object>> updatedData) {

		Map<String, Object> notifications = updatedData.stream().findFirst().get();
		Map<String, Object> settings = ImmutableMap.of("notifications", notifications);

		String customerId = customersRegistry.getCustomerId(customerIdCard);
		Response settingsResponse = restClient.put("settings", settings, ImmutableMap.of("x-customer-id", customerId));

		cucumberContext.publishValue("response", settingsResponse);
		cucumberContext.publishValue("responseEntity", restJsonReader.read(settingsResponse));
	}

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private CustomersRegistry customersRegistry;

	@Autowired
	private RestClient restClient;

	@Autowired
	private RestJsonReader restJsonReader;
}
