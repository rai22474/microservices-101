package stepdefinitions.cards;

import cucumber.api.java.en.Then;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.customers.CustomersRegistry;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenCardHasData {

    @Then("^the customer \"(.*?)\" \"(.*?)\" card status has changed to \"(.*?)\"$")
    public void the_customer_card_status_has_changed_to(String customerIdCard, String cardType, String expectedStatus) {
        String cardId = getCardId(customerIdCard, cardType);
        Map<String, Object> returnedCardData = getCardById(customerIdCard, cardId);

        assertTrue("Card must contain a status property.", returnedCardData.containsKey("status"));
        assertEquals("Card status must have changed to " + expectedStatus, expectedStatus, returnedCardData.get("status"));
    }

    private String getCardId(String customerIdCard, String cardType) {
        String customerId = customersRegistry.getCustomerId(customerIdCard);
        return cardsRegistry.get(customerId, cardType);
    }

    private Map<String, Object> getCardById(String customerIdCard, String cardId) {

        Map<String, Object> headers = new HashMap<>();
        headers.put("x-customer-id", customersRegistry.getCustomerId(customerIdCard));

        Response cardsResponse = restClient.get("cards/" + cardId, headers);
        assertEquals("Cards for " + customersRegistry.getCustomerId(cardId) + " must exist.", 200, cardsResponse.getStatus());

        return restJsonReader.read(cardsResponse);
    }

    @Autowired
    private CardsRegistry cardsRegistry;

    @Autowired
    private CustomersRegistry customersRegistry;

    @Autowired
    private RestClient restClient;

    @Autowired
    private RestJsonReader restJsonReader;

}
