package io.ari.moneyRequests.domain.repositories.assemblers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.moneyOrders.domain.recipients.repositories.assemblers.RecipientsStorageAssembler;
import io.ari.moneyRequests.domain.MoneyRequest;
import io.ari.repositories.assemblers.StorageAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Component
public class MoneyRequestsStorageAssembler extends StorageAssembler<MoneyRequest> {

	public Map<String, Object> convertEntityToDto(MoneyRequest moneyRequest) {
		Map<String, Object> moneyRequestDto = newHashMap();
		moneyRequestDto.put("amount", getAmountData(moneyRequest));
		moneyRequestDto.put("status", moneyRequest.getStatus());
		moneyRequestDto.put("id", moneyRequest.getId());
		moneyRequestDto.put("bucksId", moneyRequest.getBucksId());
		moneyRequestDto.putAll(getRecipientData(moneyRequest));

		return ImmutableMap.copyOf(moneyRequestDto);
	}

	public MoneyRequest convertDtoToEntity(Map<String, Object> moneyRequestData) {
		MoneyRequest moneyRequest = new MoneyRequest((String) moneyRequestData.get("id"));
		moneyRequest.setBucksId((String) moneyRequestData.get("bucksId"));
		moneyRequest.setAmount(getAmount(moneyRequestData));
		moneyRequest.setRecipient(getRecipient(moneyRequestData));
		moneyRequest.setStatus((String) moneyRequestData.get("status"));

		return moneyRequest;
	}

	private Map<String, Object> getAmountData(MoneyRequest moneyRequest) {
		return moneyStorageAssembler.convertEntityToDto(moneyRequest.getAmount());
	}

	private Map<String, Object> getRecipientData(MoneyRequest moneyRequest) {
		return recipientsStorageAssembler.convertEntityToDto(moneyRequest.getRecipient());
	}

	private Money getAmount(Map<String, Object> moneyRequestData) {
		return moneyStorageAssembler.convertDtoToEntity((Map<String, Object>) moneyRequestData.get("amount"));
	}

	private Recipient getRecipient(Map<String, Object> moneyRequestData) {
		Map<String, Object> recipientData = Maps.filterKeys(moneyRequestData, key -> !(key.equals("amount")
				|| key.equals("id")
				|| key.equals("status")
				|| key.equals("bucksId")));
		return recipientsStorageAssembler.convertDtoToEntity(recipientData);
	}

	@Autowired
	@Qualifier("moneyStorageAssembler")
	private StorageAssembler<Money> moneyStorageAssembler;

	@Autowired
	private RecipientsStorageAssembler recipientsStorageAssembler;
}
