package stepdefinitions.customers;

import com.google.common.collect.ImmutableMap;
import cucumber.api.java.en.When;
import io.ari.CucumberContext;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@ContextConfiguration("classpath:cucumber.xml")
public class WhenPersonRegisters {

	@When("^a person is registered in the system with the following data:$")
	public void a_person_is_registered_in_the_system_with_the_following_data(List<Map<String, Object>> customersData) {
		Map<String, Object> customerData = customersData.stream().findFirst().get();

		Map<String, Object> customer = createCustomer(customerData);
		Response customerResponse = restClient.post("customers", customer, ImmutableMap.of("x-customer-id", customerData.get("id")));

		cucumberContext.publishValue("response", customerResponse);
		Map<String, Object> responseEntity = restJsonReader.read(customerResponse);
		cucumberContext.publishValue("responseEntity", responseEntity);
		customersRegistry.registerCustomer((String) customerData.get("id"),(String) customerData.get("idCard"));
	}

	private Map<String, Object> createCustomer(Map<String, Object> customerData) {
		Map<String, Object> customerDataWithoutId = newHashMap(customerData);
		customerDataWithoutId.remove("id");
        Object termsAndConditions = customerDataWithoutId.get("termsAndConditions");
        if(termsAndConditions != null){
            customerDataWithoutId.put("termsAndConditions", new Boolean(termsAndConditions.toString()));
        }

		return customerDataWithoutId;
	}

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private RestClient restClient;

	@Autowired
	private CustomersRegistry customersRegistry;

	@Autowired
	private RestJsonReader restJsonReader;

}
