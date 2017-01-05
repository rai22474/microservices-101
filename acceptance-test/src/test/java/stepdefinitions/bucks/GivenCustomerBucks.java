package stepdefinitions.bucks;

import com.google.common.collect.ImmutableMap;
import cucumber.api.java.en.Given;
import io.ari.RestClient;
import io.ari.RestJsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import stepdefinitions.MoneyFactory;
import stepdefinitions.customers.CustomersRegistry;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@ContextConfiguration("classpath:cucumber.xml")
public class GivenCustomerBucks {

	@Given("^the customer \"(.*?)\" has the following money savings kept in the system:$")
	public void the_customer_have_the_following_money_saving_keep_in_the_systems(String identityCard, List<Map<String, String>> rechargeDataList) {
		Map<String, String> rechargeData = rechargeDataList.stream().findFirst().get();
		String customerId = customersRegistry.getCustomerId(identityCard);

		Map<String,Object> recharge = createRecharge(rechargeData);
		Response response = restClient.post("recharges",
				recharge,
				ImmutableMap.of("x-customer-id", customerId));

		assertEquals("The agreement POST return an OK", 201, response.getStatus());

		checkCustomerBucks(customerId,recharge);
	}

	private void checkCustomerBucks(String customerId,Map<String,Object> recharge){
		Map<String, Object> headers = new HashMap<>();
		headers.put("x-customer-id", customerId);

		Response buckResponse = restClient.get("bucks", headers);

		assertEquals("Bucks for " + customerId + " must exist.", 200, buckResponse.getStatus());
		Map<String,Object> returnedBucks = restJsonReader.read(buckResponse);

		assertNotNull("The returned buck must not be null",returnedBucks);
		assertEquals("Returned bucks total", recharge, ((Map<String, Object>) returnedBucks.get("balance")).get("total"));
	}

	private Map<String, Object> createRecharge(Map<String, String> rechargeData) {
		return moneyFactory.createMoney(rechargeData.get("recharge"));
	}

	@Autowired
	private RestClient restClient;

	@Autowired
	private RestJsonReader restJsonReader;

	@Autowired
	private CustomersRegistry customersRegistry;

	@Autowired
	private MoneyFactory moneyFactory;
}