package stepdefinitions.orderMoney;

import static com.google.common.collect.Maps.newHashMap;

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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


@Component
@Scope("cucumber-glue")
public class MoneyOrdersFactory extends DefaultMoneyFactory {

	public Map<String, Object> createSingleMoneyOrderBundle(String recipientIdCard, String amount, String reason) {
		Map<String, Object> data = newHashMap();

		data.put("reason", reason);
		data.put("mobilePhone", getCustomer(recipientIdCard).get().get("phone"));
        data.put("name", getCustomer(recipientIdCard).get().get("name"));
		data.put("amount", amount);

		return createMoneyOrderBundleFromRawProperties(ImmutableList.of(data));
	}

	public Map<String, Object> createMoneyOrderBundle(String reason, List<Map<String, Object>> moneyOrdersData) {

		return createMoneyOrderBundleFromRawProperties(moneyOrdersData
				.stream()
				.map(data -> {
					Map<String, Object> completeOrderData = newHashMap(data);
					completeOrderData.putIfAbsent("reason", reason);
					return completeOrderData;
				})
				.collect(Collectors.toList()));
	}

	private Map<String, Object> createMoneyOrderBundleFromRawProperties(List<Map<String, Object>> data) {
		Map<String, Object> moneyOrders = newHashMap();
		copyIfContains("reason", data.stream().findFirst().get(), moneyOrders);

		moneyOrders.put("moneyOrders", data
				.stream()
				.map(this::createMoneyOrderFromRawProperties)
				.collect(Collectors.toList()));

		return moneyOrders;
	}

	private Map<String, Object> createMoneyOrderFromRawProperties(Map<String, Object> data) {
		Map<String, Object> moneyOrder = newHashMap();
		copyIfContains("amount", data, moneyOrder);
		copyIfContains("name", data, moneyOrder);
		copyIfContains("mobilePhone", data, moneyOrder);
		copyIfContains("email", data, moneyOrder);
		copyIfContains("accountNumber", data, moneyOrder);
		copyIfContains("unknownProperty", data, moneyOrder);
		return replaceAmount(moneyOrder);
	}

	private Map<String, Object> replaceAmount(Map<String, Object> moneyOrderData) {
		HashMap<String, Object> moneyOrder = newHashMap(moneyOrderData);
		moneyOrder.computeIfPresent("amount", (key, value) -> moneyFactory.createMoney((String) value));
		return ImmutableMap.copyOf(moneyOrder);
	}

	private void copyIfContains(String key, Map<String, Object> source, Map<String, Object> target) {
		if (source.containsKey(key)) {
			target.put(key, source.get(key));
		}
	}

	private Optional<Map<String, String>> getCustomer(String recipientIdCard) {
		List<Map<String, String>> customers = (List<Map<String, String>>) cucumberContext.getValue("customers");
		Optional<Map<String, String>> customer = customers.stream().filter(customerData -> customerData.get("identityCard").equals(recipientIdCard)).findFirst();
		return customer;
	}

    @Override
    public String getEntityName() {
        return "moneyOrders";
    }

	@Autowired
	private MoneyFactory moneyFactory;

	@Autowired
	private CucumberContext cucumberContext;

}
