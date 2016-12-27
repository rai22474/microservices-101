package io.ari.customers.resources.assemblers;

import com.google.common.collect.ImmutableSet;
import io.ari.assemblers.Assembler;
import io.ari.assemblers.HypermediaAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

@Component
public class CustomersAssembler extends Assembler {

	@Override
	public Map<String, Object> convertEntityToDto(Map<String, Object> customerData, Object... additionalData) {
		Map<String, Object> customerDto = customerData
				.entrySet()
				.stream()
				.filter(entry -> !blacklistedKeys.contains(entry.getKey()))
				.collect(toMap(entry -> entry.getKey(), entry -> entry.getValue()));

		customerDto.put("_links", getCustomerHypermedia());

		return customerDto;
	}

	@Override
	protected String getCollectionSelfLink() {
		return "api/customers";
	}

	private Map<String, Object> getCustomerHypermedia() {
		Map<String, Object> hypermedia = hypermediaAssembler.createHypermedia("api/me", "wizzo-read");

		hypermedia.put("bucks", hypermediaAssembler.createLink("api/bucks", "GET", "wizzo-read"));
		hypermedia.put("movements", hypermediaAssembler.createLink("api/movements", "GET", "wizzo-read"));
		hypermedia.put("settings", hypermediaAssembler.createLink("api/settings", "GET", "wizzo-read"));
		hypermedia.put("editSettings", hypermediaAssembler.createLink("api/settings", "PUT", "wizzo-write"));
		hypermedia.put("editMe", hypermediaAssembler.createLink("api/me", "PUT", "wizzo-write"));

		hypermedia.put("createMoneyOrder", hypermediaAssembler.createLink("api/moneyOrders", "POST", "wizzo-write"));
		hypermedia.put("createMoneyOrderDraft", hypermediaAssembler.createLink("api/drafts/moneyOrders", "POST", "wizzo-write"));

		hypermedia.put("createMoneyRequest", hypermediaAssembler.createLink("api/moneyRequests", "POST", "wizzo-write"));
		hypermedia.put("createMoneyRequestDraft", hypermediaAssembler.createLink("api/drafts/moneyRequests", "POST", "wizzo-write"));

		hypermedia.put("recharge", hypermediaAssembler.createLink("api/recharges", "POST", "wizzo-recharges"));
		hypermedia.put("rechargeCards", hypermediaAssembler.createLink("api/cards", "GET", "wizzo-recharges"));

		hypermedia.put("wallet", hypermediaAssembler.createLink("api/cards", "GET", "wizzo-read"));
		hypermedia.put("createCard", hypermediaAssembler.createLink("api/cards", "POST", "wizzo-write"));

		return hypermedia;
	}

	@Autowired
	private HypermediaAssembler hypermediaAssembler;

	private static final Set<String> blacklistedKeys = ImmutableSet.of(
			"settings");
}
