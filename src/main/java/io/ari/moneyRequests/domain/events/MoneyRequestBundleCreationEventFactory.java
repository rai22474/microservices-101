package io.ari.moneyRequests.domain.events;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MoneyRequestBundleCreationEventFactory {

	public Map<String, Object> createEvent(MoneyRequestBundle moneyRequestBundle) {
		String bucksId = moneyRequestBundle.getBucksId();

		Map<String, Object> movementData = Maps.newHashMap();
		movementData.put("sourceCommand", moneyRequestBundle.getId());
		movementData.put("agree", bucksId);
		movementData.put("_relatedAgreements", ImmutableList.of(bucksId));
		movementData.put("movementType", MOVEMENT_TYPE);
		//movementData.put("amount", moneyStorageAssembler.convertEntityToDto(moneyRequestBundle.calculateAmount()));
		assignReason(moneyRequestBundle, movementData);
		/*movementData.put("moneyRequests", moneyRequestBundle.getRequests()
				.stream()
				.map(moneyRequestsStorageAssembler::convertDomainEntityToDto)
				.collect(Collectors.toList()));
*/
		return movementData;
	}

	private void assignReason(MoneyRequestBundle moneyOrderBundle, Map<String, Object> movementData) {
		if (moneyOrderBundle.getReason() != null) {
			movementData.put("reason", moneyOrderBundle.getReason());
		}
	}

	private static final String MOVEMENT_TYPE = "moneyRequestBundle";

}
