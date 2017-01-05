package stepdefinitions.orderMoney;

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
public class MoneyOrdersChecker {

	public void verifyMoneyOrderBundle(List<Map<String, String>> expectedBundles) {
		Map<String, String> expectedMoneyBundle = expectedBundles.stream().findFirst().get();
		Map<String, Object> moneyOrderBundle = (Map<String, Object>) cucumberContext.getValue("responseEntity");

		assertEquals("The bundle amount is not the expected", moneyFactory.createMoney(expectedMoneyBundle.get("amount")), moneyOrderBundle.get("amount"));
		assertEquals("The reason is not the expected", expectedMoneyBundle.get("reason"), moneyOrderBundle.get("reason"));
	}

	@Autowired
	private MoneyFactory moneyFactory;

	@Autowired
	private CucumberContext cucumberContext;
}
