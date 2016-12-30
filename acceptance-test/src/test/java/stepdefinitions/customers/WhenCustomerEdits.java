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
public class WhenCustomerEdits {

    @When("^the customer \"([^\"]*)\" updates his address to:$")
    public void the_customer_updates_his_address_to(String customerIdCard,  List<Map<String, Object>> customersData) {
        Map<String, Object> customerAddress = customersData.stream().findFirst().get();
        Map<String, Object> customer = ImmutableMap.of("name","Chino", "lastName", "Cudeiro", "address", customerAddress);
		putCustomer(customer,customerIdCard);
    }

    @When("^the customer \"([^\"]*)\" updates his name to \"([^\"]*)\" and last name to \"([^\"]*)\"$")
    public void the_customer_updates_his_name_to_and_last_name_to(String customerIdCard, String name, String lastName) {
        Map<String, Object> customer = ImmutableMap.of("name",name, "lastName", lastName);
		putCustomer(customer,customerIdCard);
    }

	@When("^the customer \"([^\"]*)\" updates his email to \"([^\"]*)\"$")
	public void the_customer_updates_his_email_to(String customerIdCard, String email) {
		Map<String, Object> customer = ImmutableMap.of("name","Chino", "lastName", "Cudeiro", "email", email);
		putCustomer(customer,customerIdCard);
	}

	private void putCustomer(Map<String, Object> customer, String customerIdCard){
		String customerId = customersRegistry.getCustomerId(customerIdCard);
		Response customerResponse = restClient.put("me", customer, ImmutableMap.of("x-customer-id", customerId));

		cucumberContext.publishValue("response", customerResponse);
		Map<String, Object> responseEntity = restJsonReader.read(customerResponse);
		cucumberContext.publishValue("responseEntity", responseEntity);
	}

    @When("^the customer \"([^\"]*)\" update his data to:$")
    public void the_customer_update_his_data_to(String customerIdCard, List<Map<String, Object>> customersData) throws Throwable {
        Map<String, Object> customer = customerFactory.createDefaultBundle(customersData);

        String customerId = customersRegistry.getCustomerId(customerIdCard);
        Response customerResponse = restClient.put("me", customer, ImmutableMap.of("x-customer-id", customerId));

        cucumberContext.publishValue("response", customerResponse);
        Map<String, Object> responseEntity = restJsonReader.read(customerResponse);
        cucumberContext.publishValue("responseEntity", responseEntity);
    }

    @Autowired
    private CustomersFactory customerFactory;

    @Autowired
    private CucumberContext cucumberContext;

    @Autowired
    private CustomersRegistry customersRegistry;

    @Autowired
    private RestClient restClient;

    @Autowired
    private RestJsonReader restJsonReader;
}
