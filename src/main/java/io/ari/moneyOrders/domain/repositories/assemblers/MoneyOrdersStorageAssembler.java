package io.ari.moneyOrders.domain.repositories.assemblers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.moneyOrders.domain.recipients.repositories.assemblers.RecipientsStorageAssembler;
import io.ari.repositories.assemblers.StorageAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Component
public class MoneyOrdersStorageAssembler extends StorageAssembler<MoneyOrder> {

	public Map<String, Object> convertEntityToDto(MoneyOrder moneyOrder) {
		Map<String, Object> moneyOrderDto = newHashMap();
		moneyOrderDto.put("amount", getAmountData(moneyOrder));
		moneyOrderDto.put("bucksId", moneyOrder.getBucksId());
		moneyOrderDto.put("id", moneyOrder.getId());
		moneyOrderDto.put("status", moneyOrder.getStatus());
		moneyOrderDto.putAll(getRecipientData(moneyOrder));

		return ImmutableMap.copyOf(moneyOrderDto);
	}

	public MoneyOrder convertDtoToEntity(Map<String, Object> moneyOrderData) {
		MoneyOrder moneyOrder = new MoneyOrder((String) moneyOrderData.get("id"));
		
		moneyOrder.setAmount(getAmount(moneyOrderData));
		moneyOrder.setRecipient(getRecipient(moneyOrderData));
		moneyOrder.setBucksId((String) moneyOrderData.get("bucksId"));
		moneyOrder.setStatus((String) moneyOrderData.get("status"));
		
		return moneyOrder;
	}

	private Map<String, Object> getAmountData(MoneyOrder moneyOrder) {
		return moneyStorageAssembler.convertEntityToDto(moneyOrder.getAmount());
	}

	private Map<String, Object> getRecipientData(MoneyOrder moneyOrder) {
		return partiesStorageAssembler.convertEntityToDto(moneyOrder.getRecipient());
	}

	private Money getAmount(Map<String, Object> moneyOrderData) {
		return moneyStorageAssembler.convertDtoToEntity((Map<String, Object>) moneyOrderData.get("amount"));
	}

	private Recipient getRecipient(Map<String, Object> moneyOrderData) {
		Map<String, Object> recipientData = Maps.filterKeys(moneyOrderData,
				key -> !(key.equals("amount") || key.equals("id") || key.equals("bucksId") || key.equals("status")));
		return partiesStorageAssembler.convertDtoToEntity(recipientData);
	}

	@Autowired
	@Qualifier("moneyStorageAssembler")
	private StorageAssembler<Money> moneyStorageAssembler;

	@Autowired
	private RecipientsStorageAssembler partiesStorageAssembler;
}
