package io.ari.cards.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.assemblers.Assembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Component
public class CardsReadAssembler extends Assembler {

	@Override
	public Map<String, Object> convertEntityToDto(Map<String, Object> entityData, Object... additionalData) {
		HashMap<String, Object> cardDto = newHashMap(entityData);
		cardDto.put("_links", hypermediaAssembler.getHypermedia(entityData));
		cardDto.remove("participants");

		return ImmutableMap.copyOf(cardDto);
	}

	@Override
	protected String getCollectionSelfLink() {
		return SELF;
	}

	private static String SELF = "api/cards";

	@Autowired
	private CardHypermediaAssembler hypermediaAssembler;

}

