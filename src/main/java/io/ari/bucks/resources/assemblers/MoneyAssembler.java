package io.ari.bucks.resources.assemblers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ari.money.domain.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MoneyAssembler {

	public Map<String, Object> convertEntityToDto(Money money) {
		return objectMapper.convertValue(money, Map.class);
	}

	public Money convertDtoToEntity(Map<String, Object> moneyData) {
		return new Money((Number) moneyData.get("value"), (String) moneyData.get("currency"));
	}

	void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Autowired
	private ObjectMapper objectMapper;
}
