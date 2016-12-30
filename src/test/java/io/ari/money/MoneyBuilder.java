package io.ari.money;

import com.google.common.collect.ImmutableMap;
import io.ari.money.domain.Money;


import java.math.BigDecimal;
import java.util.Map;

public class MoneyBuilder {

	private MoneyBuilder(String amount) {
		this.amount = amount;
	}

	public static MoneyBuilder val(String amount) {
		return new MoneyBuilder(amount);
	}

	public MoneyBuilder eur() {
		return this;
	}

	public Money entity() {
		return new Money(new BigDecimal(amount), currency);
	}

	public Map<String, Object> dto() {
		return ImmutableMap.of("value", new BigDecimal(amount), "currency", currency);
	}

	private String currency = "EUR";

	private String amount;
}
