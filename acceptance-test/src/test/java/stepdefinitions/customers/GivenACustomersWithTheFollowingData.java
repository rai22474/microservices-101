package stepdefinitions.customers;

import com.google.common.collect.ImmutableMap;
import cucumber.api.java.en.Given;
import io.ari.CucumberContext;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.cards.CardsRegistry;

import javax.ws.rs.core.Response;
import java.util.*;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:cucumber.xml")
public class GivenACustomersWithTheFollowingData {

	@Given("^a customers with the following data:$")
	public void a_customers_with_the_following_data(List<Map<String, Object>> customers) {
		customers.forEach(this::registerCustomer);
		cucumberContext.publishValue("customers", customers);
	}

	private void registerCustomer(Map<String, Object> customer) {
		Map<String, Object> fullCustomer = createFullCustomer(customer);

		postCustomer(fullCustomer);

		customersRegistry.registerCustomer((String) fullCustomer.get("id"),
				(String) fullCustomer.get("idCard"));

		registerCreatedCard(fullCustomer);
	}

	private void registerCreatedCard(Map<String, Object> fullCustomer) {
		String customerId = (String) fullCustomer.get("id");

		Map<String, Object> headers = new HashMap<>();
		headers.put("x-customer-id", customerId);

		Response cardsResponse = restClient.get("cards", headers);
		assertEquals("Cards for " + customerId + " must exist.", 200, cardsResponse.getStatus());

		Map<String, Object> agreementsResponseMap = restJsonReader.read(cardsResponse);
		Collection<Map<String,Object>> customerCards = (Collection<Map<String, Object>>) agreementsResponseMap.get("items");

		customerCards
				.forEach(card -> cardsRegistry.add(customerId,
						(String) card.get("type"),
						(String) card.get("id"))
				);
	}

	private void postCustomer(Map<String, Object> customerData) {
		Response response = restClient.post("customers", customerData,
				ImmutableMap.of("x-customer-id", customerData.get("id")));

		assertEquals("The response must be the expected", 201, response.getStatus());
	}

	private Map<String, Object> createFullCustomer(Map<String, Object> customerData) {
		Map<String, Object> fullCustomer = newHashMap(customerData);

		fullCustomer.putIfAbsent("id", UUID.randomUUID().toString());
		fullCustomer.put("idCard", customerData.get("identityCard"));
		fullCustomer.remove("identityCard");
		fullCustomer.put("mobilePhone", customerData.get("phone"));
		fullCustomer.remove("phone");
		fullCustomer.putIfAbsent("termsAndConditions", true);
		fullCustomer.putIfAbsent("bankingServiceAgreementId", UUID.randomUUID().toString());
		fullCustomer.putIfAbsent("bankingServiceCardId", UUID.randomUUID().toString());

		return fullCustomer;
	}

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private RestClient restClient;

	@Autowired
	private CustomersRegistry customersRegistry;

	@Autowired
	private RestJsonReader restJsonReader;

	@Autowired
	private CardsRegistry cardsRegistry;
}
