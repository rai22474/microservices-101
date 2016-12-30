package stepdefinitions.customers;


import cucumber.api.java.en.Given;
import io.ari.RestClient;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;

@ContextConfiguration("classpath:cucumber.xml")
public class GivenACustomerAddress {
	@Given("^the customer \"([^\"]*)\" has the following address in wizzo:$")
	public void the_customer_has_the_following_address_in_wizzo(String customerIdCard, List<Map<String, Object>> addressData) {
		Map<String, Object> customer = ImmutableMap.of("name", "Chino", "lastName", "Cudeiro", "address", addressData.stream().findFirst().get());

		String customerId = customersRegistry.getCustomerId(customerIdCard);
		writeClient.put("me", customer, ImmutableMap.of("x-customer-id", customerId));
	}

	@Autowired
	private CustomersRegistry customersRegistry;

	@Autowired
	private RestClient writeClient;
}
