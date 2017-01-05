package io.ari.moneyOrders.domain.repositories.assemblers;

import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.repositories.assemblers.StorageAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MoneyOrderBundlesStorageAssembler extends StorageAssembler<MoneyOrderBundle> {

	public Map<String, Object> convertEntityToDto(MoneyOrderBundle moneyOrderBundle) {
		Collection<Map<String, Object>> moneyOrders = moneyOrderBundle.getOrders().stream()
				.map(moneyOrdersStorageAssembler::convertEntityToDto)
				.collect(Collectors.toList());

		Map<String, Object> moneyOrderBundleDto = new HashMap<>();
		moneyOrderBundleDto.put("id", moneyOrderBundle.getId());
		moneyOrderBundleDto.put("agree", moneyOrderBundle.getBucksId());
		moneyOrderBundleDto.put("moneyOrders", moneyOrders);
		moneyOrderBundleDto.put("amount", moneyStorageAssembler.convertEntityToDto(moneyOrderBundle.calculateAmount()));
		moneyOrderBundleDto.put("creationDate", moneyOrderBundle.getCreationDate());
		assignReasonToDto(moneyOrderBundle, moneyOrderBundleDto);
		assignSourceCommandToDto(moneyOrderBundle,moneyOrderBundleDto);
		
		return moneyOrderBundleDto;
	}

	public MoneyOrderBundle convertDtoToEntity(Map<String, Object> moneyOrderBundleDto) {
		Collection<MoneyOrder> moneyOrders = moneyOrdersStorageAssembler.convertDtosToDomainEntities((Collection<Map<String, Object>>) moneyOrderBundleDto.get("moneyOrders"));
		MoneyOrderBundle bundle = new MoneyOrderBundle(
				(String) moneyOrderBundleDto.get("id"),
				(Date) moneyOrderBundleDto.get("creationDate"),
				(String) moneyOrderBundleDto.get("agree"),
				moneyOrders.toArray(new MoneyOrder[]{}));
		bundle.setReason((String) moneyOrderBundleDto.get("reason"));
		bundle.setSourceCommand((String) moneyOrderBundleDto.get("sourceCommand"));
		
		return bundle;
	}

	private void assignSourceCommandToDto(MoneyOrderBundle moneyOrderBundle, Map<String, Object> moneyOrderBundleDto) {
		if (moneyOrderBundle.getSourceCommand() != null) {
			moneyOrderBundleDto.put("sourceCommand", moneyOrderBundle.getSourceCommand());
		}
	}
	
	private void assignReasonToDto(MoneyOrderBundle moneyOrderBundle, Map<String, Object> moneyOrderBundleDto) {
		if (moneyOrderBundle.getReason() != null) {
			moneyOrderBundleDto.put("reason", moneyOrderBundle.getReason());
		}
	}

	@Autowired
	private MoneyOrdersStorageAssembler moneyOrdersStorageAssembler;

	@Autowired
	@Qualifier("moneyStorageAssembler")
	private StorageAssembler<Money> moneyStorageAssembler;
}

