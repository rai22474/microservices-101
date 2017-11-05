package io.ari.moneyRequests.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.ari.assemblers.HypermediaAssembler;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.repositories.exceptions.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MoneyRequestBundleDraftsAssembler {

	public MoneyRequestBundle convertDtoToEntity(String customerId, Map<String, Object> moneyRequestBundleDto) throws EntityNotFound {
		MoneyRequestBundle moneyRequestBundle = moneyRequestBundleAssembler.convertDtoToEntity(customerId, moneyRequestBundleDto);
		moneyRequestBundle.setStatus("draft");

		return moneyRequestBundle;
	}

	public Map<String, Object> convertEntityToDto(MoneyRequestBundle moneyRequestBundle) {
		Map<String, Object> moneyRequestBundleDto = moneyRequestBundleAssembler.convertEntityToDto(moneyRequestBundle);

		String selfUri = "api/drafts/moneyRequests/" + moneyRequestBundle.getId();
		Map<String, Object> links = ImmutableMap.of("editMoneyRequestDraft", hypermediaAssembler.createLink(selfUri, "PUT", "ari-write"));

		HashMap<String, Object> draftDto = Maps.newHashMap(moneyRequestBundleDto);
		draftDto.put("_links", links);
		return draftDto;
	}

	@Autowired
	private MoneyRequestBundlesAssembler moneyRequestBundleAssembler;

	@Autowired
	private MoneyRequestsAssembler moneyRequestsAssembler;

	@Autowired
	private HypermediaAssembler hypermediaAssembler;
}
