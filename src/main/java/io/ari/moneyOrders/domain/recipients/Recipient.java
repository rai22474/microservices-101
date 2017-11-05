package io.ari.moneyOrders.domain.recipients;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.ari.bucks.domain.Bucks;
import io.ari.money.domain.Money;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({@JsonSubTypes.Type(value = BucksRecipient.class, name = "bucksRecipient"),@JsonSubTypes.Type(value = AccountsRecipient.class, name = "accountsRecipient")})
public abstract class Recipient {

	public Recipient(Map<String, Object> data) {
		this.data = data;
	}

	public abstract String requestMoneyOrder(Bucks bucks, Money amount, String operationId);

	public abstract void confirmMoneyOrder(Bucks bucks, Map<String, Object> event);

	public abstract String submitMoneyRequest(String sourceBucks, Money amount, String reason, String sourceCommand);

	public abstract String getType();

	public abstract boolean isTheSameAs(String bucksId);

	public abstract Recipient clone();
	
	public void putData(Map<String, Object> recipientData) {
		data.putAll(recipientData);
	}

	public Map<String, Object> getData() {
		return data;
	}

	private Map<String, Object> data = newHashMap();
}
