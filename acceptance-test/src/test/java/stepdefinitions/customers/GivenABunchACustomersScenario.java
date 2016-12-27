package stepdefinitions.customers;

import io.ari.CucumberContext;
import io.ari.RestClient;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:cucumber.xml")
public class GivenABunchACustomersScenario {

    @Given("^an bunch of customers with identity cards \"(.*?)\"$")
    public void an_bunch_of_customers_with_identity_cards(String identityCards) {
        String[] identityCardsAsArrays = identityCards.split(",");

        Map<String, String> customerIdentifiers = new HashMap<>();

        for (String identityCard : identityCardsAsArrays) {
            Response response = requestSender.post("customers", customersFactory.createCustomer(identityCard));
            assertEquals("The response status must be created", 201, response.getStatus());

            Map<String, Object> customerCreationResponse = response.readEntity(new GenericType<Map<String, Object>>() {
            });
            String customerId = (String) customerCreationResponse.get("entityId");
            customerIdentifiers.put(identityCard, customerId);

            customersRegistry.registerCustomer(customerId,identityCard);
        }

        cucumberContext.publishValue("customerIdentifiers", customerIdentifiers);
    }

    @After(value = "@deleteCustomer", order = 10)
    public void deleteRegisterClients() {
        customersRegistry.deleteRegisterCustomers();
    }

    @Autowired
    private RestClient requestSender;

    @Autowired
    private CustomersFactory customersFactory;

    @Autowired
    private CustomersRegistry customersRegistry;

    @Autowired
    private CucumberContext cucumberContext;
}
