package stepdefinitions.customers;

import cucumber.api.java.en.When;
import io.ari.CucumberContext;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@ContextConfiguration("classpath:cucumber.xml")
public class WhenRequestCustomerDataWithoutHeader {

	@When("^i request the customer data without costumer header$")
	public void i_request_the_customer_data_without_custumer_header()  {
		Map<String, Object> headers = new HashMap<>();
		Response response = requestSender.get("me", headers);

		cucumberContext.publishValue("response", response);
		cucumberContext.publishValue("responseEntity", restJsonReader.read(response));
	}

	@Autowired
	private RestClient requestSender;

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private RestJsonReader restJsonReader;
}
