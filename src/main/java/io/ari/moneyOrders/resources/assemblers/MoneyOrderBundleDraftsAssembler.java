package io.ari.moneyOrders.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.ari.assemblers.HypermediaAssembler;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.repositories.exceptions.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class MoneyOrderBundleDraftsAssembler {

	public MoneyOrderBundle convertDtoToEntity(String customerId, Map<String, Object> draftData) throws EntityNotFound {
		return assignDraftStatus(moneyOrderBundleAssembler.convertDtoToEntity(customerId, draftData));
	}

	public MoneyOrderBundle convertDtoToExistingEntity(String customerId, String draftId, Map<String, Object> draftData) throws EntityNotFound {
		return assignDraftStatus(moneyOrderBundleAssembler.convertDtoToExistingEntity(customerId, draftId, draftData));
	}

	public Map<String, Object> convertEntityToDto(MoneyOrderBundle moneyOrderBundle) {
		Map<String, Object> moneyOrderBundleDto = moneyOrderBundleAssembler.convertEntityToDto(moneyOrderBundle);

		String selfUri = "api/drafts/moneyOrders/" + moneyOrderBundle.getId();
		Map<String, Object> links = ImmutableMap.of(
				"editMoneyOrderDraft", hypermediaAssembler.createLink(selfUri, "PUT", "wizzo-write"));

		HashMap<String, Object> draftDto = Maps.newHashMap(moneyOrderBundleDto);
		draftDto.put("_links", links);
		return draftDto;
	}

	private MoneyOrderBundle assignDraftStatus(MoneyOrderBundle moneyOrderBundle) {
		moneyOrderBundle.setStatus("draft");
		return moneyOrderBundle;
	}

	@Autowired
	private MoneyOrderBundlesAssembler moneyOrderBundleAssembler;

	@Autowired
	private MoneyOrdersAssembler moneyOrdersAssembler;

	@Autowired
	private HypermediaAssembler hypermediaAssembler;
}
