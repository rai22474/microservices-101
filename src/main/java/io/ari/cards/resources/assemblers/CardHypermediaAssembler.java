package io.ari.cards.resources.assemblers;

import com.google.common.collect.ImmutableList;
import io.ari.assemblers.HypermediaAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Maps.newHashMap;

@Component
public class CardHypermediaAssembler {

	public Map<String, Object> getHypermedia(Map<String, Object> card) {

		return linkNames
				.stream()
				.filter(link -> linkFilters.get(link).test(card))
				.map(link -> linkFactories.get(link).apply(card))
				.collect(
						() -> newHashMap(),
						(map1, map2) -> map1.putAll(map2),
						(map1, map2) -> map1.putAll(map2));
	}

	private String getSelfUri(Map<String, Object> card) {
		return "api/cards/" + card.get("id");
	}

	private boolean hasStatus(Map<String, Object> card, String status) {
		return status.equals(card.get(CARD_STATUS));
	}

	private boolean hasType(Map<String, Object> card, String type) {
		return type.equals(card.get(CARD_TYPE));
	}

	@Autowired
	private HypermediaAssembler hypermediaAssembler;

	private Collection<String> linkNames = ImmutableList.of(
			"self",
			"block",
			"cancel",
			"activate");

	private Map<String, Predicate<Map<String, Object>>> linkFilters = of(
			"self", card -> true,
			"block", card -> hasType(card, TYPE_TVA) && hasStatus(card, STATUS_ACTIVE),
			"cancel", card -> !hasType(card, TYPE_TVA),
			"activate", card -> hasStatus(card, STATUS_INACTIVE));

	private Map<String, Function<Map<String, Object>, Map<String, Object>>> linkFactories = of(
			"self", card -> of("self", hypermediaAssembler.createSelfLink(getSelfUri(card), "ari-read")),
			"block", card -> of("block", hypermediaAssembler.createLink(getSelfUri(card) + "/blocks", "POST", "ari-write")),
			"cancel", card -> of("cancel", hypermediaAssembler.createLink(getSelfUri(card), "DELETE", "ari-write")),
			"activate", card -> of("activate", hypermediaAssembler.createLink(getSelfUri(card) + "/activations", "POST", "ari-write")));

	private static final String CARD_STATUS = "status";

	private static final String STATUS_ACTIVE = "active";

	private static final String STATUS_INACTIVE = "inactive";

	private static final String CARD_TYPE = "cardType";

	private static final String TYPE_TVA = "tva";

}
