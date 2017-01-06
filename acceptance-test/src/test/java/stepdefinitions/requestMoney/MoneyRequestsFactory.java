package stepdefinitions.requestMoney;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.ari.CucumberContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import stepdefinitions.DefaultMoneyFactory;
import stepdefinitions.MoneyFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

@Component
@Scope(value = "cucumber-glue")
public class MoneyRequestsFactory extends DefaultMoneyFactory {

	public Map<String, Object> createMoneyRequests(String recipientIdCard, String amount, String reason) {
		Map<String, Object> moneyRequestBundle = createBaseMoneyRequest(recipientIdCard, amount);
		moneyRequestBundle.put("reason", reason);
		return moneyRequestBundle;
	}

	public Map<String, Object> createMoneyRequests(String recipientIdCard, String amount) {
		return createBaseMoneyRequest(recipientIdCard, amount);
	}
	
	public Map<String, Object> createMoneyRequests(String reason, List<Map<String, Object>> recipients) {
		List<Map<String, Object>> moneyOrders = recipients
				.stream()
				.map(this::createMoneyRequestFromMap)
				.collect(Collectors.toList());
		
		return ImmutableMap.of(
				"reason", reason,
				"moneyRequests", moneyOrders);
	}

	private Map<String, Object> createBaseMoneyRequest(String recipientIdCard, String amount) {
		Collection<Map<String, Object>> moneyRequests = new ArrayList<>();
		moneyRequests.add(createMoneyRequest(recipientIdCard, amount));
		
		Map<String, Object> moneyRequestBundle = new HashMap<>();
		moneyRequestBundle.put("moneyRequests", moneyRequests);
		return moneyRequestBundle;
	}

	private Map<String, Object> createMoneyRequest(String recipientIdCard, String amount) {
		List<Map<String, String>> customers = (List<Map<String, String>>) cucumberContext.getValue("customers");
		Optional<Map<String, String>> customer = customers.stream().filter(customerData ->
				customerData.get("identityCard").equals(recipientIdCard)).findFirst();
		String phone = customer.get().get("phone");
		String name = customer.get().get("name");

		Map<String, Object> moneyRequest = new HashMap<>();

		moneyRequest.put("mobilePhone", phone);
		moneyRequest.put("name", name);
		moneyRequest.put("type", "phoneRecipient");
		moneyRequest.put("amount", moneyFactory.createMoney(amount));

		return moneyRequest;
	}

	private Map<String, Object> createMoneyRequestFromMap(Map<String, Object> moneyRequestData) {
		HashMap<String, Object> moneyRequest = Maps.newHashMap(moneyRequestData);
		moneyRequest.replace("amount", moneyFactory.createMoney((String) moneyRequestData.get("amount")));
		return ImmutableMap.copyOf(moneyRequest);
	}

    @Override
    public String getEntityName() {
        return "moneyRequests";
    }

	@Autowired
	private MoneyFactory moneyFactory;

	@Autowired
	private CucumberContext cucumberContext;


}
