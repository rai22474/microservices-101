package stepdefinitions.customers;

import cucumber.api.java.en.Given;
import io.ari.CucumberContext;
import io.ari.RestClient;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

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

            Map<String,Object> customerData = customersFactory.createCustomer(identityCard);
            Response response = requestSender.post("customers",customerData,ImmutableMap.of("x-customer-id", customerData.get("id")));

            assertEquals("The response status must be created", 201, response.getStatus());

            customerIdentifiers.put(identityCard, (String)customerData.get("id"));
            customersRegistry.registerCustomer((String)customerData.get("id"),identityCard);
        }

        cucumberContext.publishValue("customerIdentifiers", customerIdentifiers);
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
