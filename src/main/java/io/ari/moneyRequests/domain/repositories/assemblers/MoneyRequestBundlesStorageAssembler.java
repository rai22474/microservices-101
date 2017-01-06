package io.ari.moneyRequests.domain.repositories.assemblers;

import com.google.common.collect.Maps;
import io.ari.money.domain.Money;
import io.ari.moneyRequests.domain.MoneyRequest;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.repositories.assemblers.StorageAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MoneyRequestBundlesStorageAssembler extends StorageAssembler<MoneyRequestBundle>{
	
	public Map<String, Object> convertEntityToDto(MoneyRequestBundle moneyRequestBundle) {
		Collection<Map<String, Object>> moneyRequests = moneyRequestBundle.getRequests().stream()
				.map(moneyRequestsStorageAssembler::convertEntityToDto)
				.collect(Collectors.toList());

		Map<String, Object> moneyRequestBundleDto = Maps.newHashMap();
		moneyRequestBundleDto.put("id", moneyRequestBundle.getId());
		moneyRequestBundleDto.put("agree", moneyRequestBundle.getBucksId());
		moneyRequestBundleDto.put("moneyRequests", moneyRequests);
		moneyRequestBundleDto.put("amount", moneyStorageAssembler.convertEntityToDto(moneyRequestBundle.calculateAmount()));
		moneyRequestBundleDto.put("creationDate", moneyRequestBundle.getCreationDate());
		moneyRequestBundleDto.put("status", moneyRequestBundle.getStatus());
		
		assignReasonToDto(moneyRequestBundle, moneyRequestBundleDto);
		assignSourceCommandToDto(moneyRequestBundle, moneyRequestBundleDto);
		
		return moneyRequestBundleDto;
	}

	public MoneyRequestBundle convertDtoToEntity(Map<String, Object> moneyRequestBundleDto) {
		Collection<MoneyRequest> moneyRequests = moneyRequestsStorageAssembler.convertDtosToDomainEntities((Collection<Map<String, Object>>) moneyRequestBundleDto.get("moneyRequests"));

		MoneyRequestBundle bundle = new MoneyRequestBundle(
				(String) moneyRequestBundleDto.get("id"),
				(Date) moneyRequestBundleDto.get("creationDate"),
				(String) moneyRequestBundleDto.get("agree"),
				moneyRequests.toArray(new MoneyRequest[]{}));
		bundle.setReason((String) moneyRequestBundleDto.get("reason"));
		bundle.setSourceCommand((String) moneyRequestBundleDto.get("sourceCommand"));
		
		return bundle;
	}

	private void assignReasonToDto(MoneyRequestBundle moneyRequestBundle, Map<String, Object> moneyRequestBundleDto) {
		if (moneyRequestBundle.getReason() != null) {
			moneyRequestBundleDto.put("reason", moneyRequestBundle.getReason());
		}
	}

	private void assignSourceCommandToDto(MoneyRequestBundle moneyRequestBundle, Map<String, Object> moneyRequestBundleDto) {
		if (moneyRequestBundle.getSourceCommand() != null) {
			moneyRequestBundleDto.put("sourceCommand", moneyRequestBundle.getSourceCommand());
		}
	}
	
	@Autowired
	private MoneyRequestsStorageAssembler moneyRequestsStorageAssembler;

	@Autowired
	@Qualifier("moneyStorageAssembler")
	private StorageAssembler<Money> moneyStorageAssembler;

}

