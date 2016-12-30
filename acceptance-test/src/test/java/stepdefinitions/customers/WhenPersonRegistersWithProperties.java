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

@ContextConfiguration("classpath:cucumber.xml")
public class WhenPersonRegistersWithProperties {
    @When("^a person is registered in the system with the following properties:$")
    public void a_person_is_registered_in_the_system_with_the_following_properties(List<Map<String, Object>> customersData) throws Throwable {

        String customerId = (String) customersData.stream().findFirst().get().get("id");

        Map<String, Object> customer = customerFactory.createDefaultBundle(customersData);

        Response customerResponse = restClient.post("customers", customer, ImmutableMap.of("x-customer-id", customerId));

        cucumberContext.publishValue("response", customerResponse);
        Map<String, Object> responseEntity = restJsonReader.read(customerResponse);
        cucumberContext.publishValue("responseEntity", responseEntity);
        customersRegistry.registerCustomer(customerId,(String) customer.get("idCard"));
    }

    @Autowired
    private CustomersFactory customerFactory;

    @Autowired
    private CucumberContext cucumberContext;

    @Autowired
    private RestClient restClient;

    @Autowired
    private CustomersRegistry customersRegistry;

    @Autowired
    private RestJsonReader restJsonReader;
}
