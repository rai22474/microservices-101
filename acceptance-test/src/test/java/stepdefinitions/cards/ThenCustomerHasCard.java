package stepdefinitions.cards;

import cucumber.api.java.en.Then;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.customers.CustomersRegistry;

import javax.ws.rs.core.Response;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenCustomerHasCard {

	@Then("^the customer \"(.*?)\" has a card with the following data:$")
	public void the_customer_has_a_card_with_the_following_data(String customerIdCard, List<Map<String, Object>> expectedCardsData) {
		String customerId = customersRegistry.getCustomerId(customerIdCard);

		Map<String, Object> headers = new HashMap<>();
		headers.put("x-customer-id", customerId);

		Response cardsResponse = requestSender.get("cards", headers);
		assertEquals("Cards for " + customerId + " must exist.", 200, cardsResponse.getStatus());

		Collection<Map<String,Object>> customerCards = getItems(cardsResponse);

		expectedCardsData.forEach(verifyCard(customerCards.iterator()));
	}

	private Collection<Map<String, Object>> getItems(Response response) {
		Map<String, Object> agreementsResponseMap = restJsonReader.read(response);
		return (Collection<Map<String, Object>>) agreementsResponseMap.get("items");
	}

	private Consumer<Map<String, Object>> verifyCard(Iterator<Map<String, Object>> returnedCardsIterator) {
		return expectedCardData -> {
			Map<String, Object> returnedCard = returnedCardsIterator.next();
			expectedCardData
					.forEach((key, value) -> {
						assertTrue("The card must have a " + key, returnedCard.containsKey(key));
						assertEquals("The card " + key + " must be the expected.", value, returnedCard.get(key));
					});
		};
	}

	@Autowired
	private CustomersRegistry customersRegistry;

	@Autowired
	private RestClient requestSender;

	@Autowired
	private RestJsonReader restJsonReader;
}