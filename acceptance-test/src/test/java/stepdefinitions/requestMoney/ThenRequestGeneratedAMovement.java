package stepdefinitions.requestMoney;

import com.google.common.collect.ImmutableMap;
import cucumber.api.java.en.Then;
import io.ari.CucumberContext;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.customers.CustomersRegistry;
import stepdefinitions.movements.MovementsRegistry;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenRequestGeneratedAMovement {

	@Then("^the money request must have generated a movement for customer id \"(.*?)\"$")
	public void the_money_request_must_have_generated_a_movement(String idCard) throws InterruptedException {
		String customerId = customersRegistry.getCustomerId(idCard);

		Response eventsResponse = restClient.get("timeline", ImmutableMap.of("x-customer-id", customerId));
		assertNotNull("Events GET cannot return a null response.", eventsResponse);
		assertEquals("Events GET must return an OK.", 200, eventsResponse.getStatus());

		Map<String, Object> responsePayload = restJsonReader.read(eventsResponse);
		Map<String, Object> movement = ((Collection<Map<String, Object>>) responsePayload.get("items")).stream().findFirst().get();
		context.publishValue("createdEvent", movement);
		movementsRegistry.add((String) movement.get("id"));
	}

	@Autowired
	private CucumberContext context;

	@Autowired
	private RestClient restClient;

	@Autowired
	private RestJsonReader restJsonReader;

	@Autowired
	private MovementsRegistry movementsRegistry;

	@Autowired
	private CustomersRegistry customersRegistry;

}
