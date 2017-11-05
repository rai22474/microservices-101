package stepdefinitions.customers;


import cucumber.api.java.en.And;
import io.ari.CucumberContext;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenCustomerIsDeleted {

	@And("^the customer \"(.*?)\" account is deleted from the system:$")
	public void customerIsDeleted(String customerIdCard) {
		String customerId = customersRegistry.getCustomerId(customerIdCard);
		Response subjectResponse = dataExtractorClient.get("subjects/" + customerId);
		assertEquals("Customer " + customerId + " must not exist.", 404, subjectResponse.getStatus());
	}

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private CustomersRegistry customersRegistry;

	@Autowired
	private RestClient dataExtractorClient;

	@Autowired
	private RestJsonReader restJsonReader;
}
