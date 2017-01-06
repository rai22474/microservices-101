package stepdefinitions.requestMoney;

import io.ari.CucumberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import stepdefinitions.MoneyFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Component
@Scope("cucumber-glue")
public class MoneyRequestsChecker {

	public void verifyRequest(List<Map<String, Object>> expectedData) {
		Map<String, Object> expectedRequestData = expectedData.stream().findFirst().get();
		Map<String, Object> returnedRequest = (Map<String, Object>) cucumberContext.getValue("responseEntity");

		Map<String, Object> expectedAmount = moneyFactory.createMoney((String) expectedRequestData.get("totalAmount"));
		Map<String, Object> amount = (Map<String, Object>) returnedRequest.get("amount");
		assertEquals("The bundle amount is not the expected", expectedAmount, amount);

		String expectedReason = (String) expectedRequestData.get("reason");
		String reason = (String) returnedRequest.get("reason");
		assertEquals("The reason is not the expected", expectedReason, reason);
	}

	@Autowired
	private CucumberContext cucumberContext;

	@Autowired
	private MoneyFactory moneyFactory;

}
