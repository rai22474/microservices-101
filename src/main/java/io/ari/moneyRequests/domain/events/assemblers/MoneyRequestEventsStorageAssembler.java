package io.ari.moneyRequests.domain.events.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.money.domain.Money;
import io.ari.moneyRequests.domain.MoneyRequest;
import io.ari.repositories.assemblers.StorageAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MoneyRequestEventsStorageAssembler {

	public Map<String, Object> convertDomainEntityToDto(MoneyRequest moneyRequest) {
		Map<String, Object> moneyRequestDto = new HashMap<>();
		moneyRequestDto.put("amount", getAmountData(moneyRequest));
		moneyRequestDto.putAll(getRecipientData(moneyRequest));

		return ImmutableMap.copyOf(moneyRequestDto);
	}

	private Map<String, Object> getAmountData(MoneyRequest moneyRequest) {
		return moneyStorageAssembler.convertEntityToDto(moneyRequest.getAmount());
	}

	private Map<String, Object> getRecipientData(MoneyRequest moneyRequest) {
		return recipientEventsStorageAssembler.convertDomainEntityToDto(moneyRequest.getRecipient());
	}

	@Autowired
	@Qualifier("moneyStorageAssembler")
	private StorageAssembler<Money> moneyStorageAssembler;

	@Autowired
	private RecipientEventsStorageAssembler recipientEventsStorageAssembler;
}
