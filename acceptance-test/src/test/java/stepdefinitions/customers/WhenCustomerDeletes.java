package stepdefinitions.customers;


import cucumber.api.java.en.When;
import io.ari.CucumberContext;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;
import java.util.Map;

@ContextConfiguration("classpath:cucumber.xml")
public class WhenCustomerDeletes {

	@When("^customer \"(.*?)\" deletes his account:$")
	public void customerDeletesHisAccount(String customerIdCard) {
		String customerId = customersRegistry.getCustomerId(customerIdCard);
		Response customerResponse = writeClient.delete("me", ImmutableMap.of("x-customer-id", customerId));

		cucumberContext.publishValue("response", customerResponse);
		Map<String, Object> responseEntity = restJsonReader.read(customerResponse);
		cucumberContext.publishValue("responseEntity", responseEntity);
	}

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private CustomersRegistry customersRegistry;

	@Autowired
	private RestClient writeClient;

	@Autowired
	private RestJsonReader restJsonReader;
}
