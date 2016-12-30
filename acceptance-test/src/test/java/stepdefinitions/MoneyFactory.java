package stepdefinitions;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class MoneyFactory {

	public Map<String, Object> createMoney(String money) {
		Map<String, Object> parsedMoney = new LinkedHashMap<>();

		String[] splitMoney = money.split(" ");

		parsedMoney.put("value", splitMoney[0].contains(".") ? new BigDecimal(splitMoney[0]) : Integer.parseInt(splitMoney[0]));
		parsedMoney.put("currency", splitMoney[1]);

		return parsedMoney;
	}

	public Map<String, Object> createMoney(BigDecimal money) {
		return ImmutableMap.of("value", money, "currency", "EUR");
	}
}
