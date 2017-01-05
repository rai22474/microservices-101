package stepdefinitions.customers;

import cucumber.api.java.en.Then;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.MoneyFactory;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:cucumber.xml")
public class ThenCustomerBucksHasLimits {

	@Then("^the customer \"(.*?)\" has the following limits into its bucks:$")
	public void the_customer_has_the_following_limits_into_its_bucks(String idCard, List<Map<String, Object>> limitsData) {
		String customerId = customersRegistry.getCustomerId(idCard);

		Map<String, Object> headers = new HashMap<>();
		headers.put("x-customer-id", customerId);

		Response buckResponse = requestSender.get("bucks", headers);

		assertEquals("Bucks for " + customerId + " must exist.", 200, buckResponse.getStatus());
		Map<String,Object> returnedBucks = restJsonReader.read(buckResponse);

		assertTrue("Customer bucks must have limits.", returnedBucks.containsKey("limits"));
		verifyRechargeLimits((Map<String, Object>) returnedBucks.get("limits"), limitsData.stream().findFirst().get());
	}

	private void verifyRechargeLimits(Map<String, Object> limits, Map<String, Object> expectedLimitsData) {
		assertTrue("Limit must have property recharge", limits.containsKey("recharge"));
		Map<String, Object> recharge = (Map<String, Object>) limits.get("recharge");

		assertTrue("Recharge limits must have property thisPeriod", recharge.containsKey("thisPeriod"));
		assertEquals("Recharge this period limit must be the expected.", moneyFactory.createMoney((String) expectedLimitsData.get("rechargeThisPeriod")), recharge.get("thisPeriod"));
		assertTrue("Recharge limits must have property last", recharge.containsKey("last"));
		assertEquals("Recharge last limit must be the expected.", moneyFactory.createMoney((String) expectedLimitsData.get("rechargeLast")), recharge.get("last"));
		assertTrue("Recharge limits must have property remaining", recharge.containsKey("remaining"));
		assertEquals("Recharge remaining limit must be the expected.", moneyFactory.createMoney((String) expectedLimitsData.get("rechargeRemaining")), recharge.get("remaining"));
		assertTrue("Recharge limits must have property max", recharge.containsKey("max"));
		assertEquals("Recharge max limit must be the expected.", moneyFactory.createMoney((String) expectedLimitsData.get("rechargeMax")), recharge.get("max"));
	}

	@Autowired
	private CustomersRegistry customersRegistry;

	@Autowired
	private MoneyFactory moneyFactory;

	@Autowired
	private RestClient requestSender;

	@Autowired
	private RestJsonReader restJsonReader;
}
