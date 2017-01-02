package io.ari.bucks.resources.assemblers;

import io.ari.assemblers.HypermediaAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BucksAssembler {
	
	public Map<String, Object> convertEntitiesToDto(Map<String, Object> bucksInformation) {
		HashMap<String, Object> bucksDto = new HashMap<>(bucksInformation);
		bucksDto.remove("participants");
		bucksDto.remove("moneyOrderSpecs");
		bucksDto.remove("moneyRequestSpecs");
		bucksDto.remove("moneyOrderSpecDrafts");
	
		bucksDto.put("_links", hypermediaAssembler.createHypermedia(SELF_URI,"ari-read"));
		
		return bucksDto;
	}

	public Map<String, Object> createEntityKey(String customerId) {
		Map<String,Object> entityKey = new HashMap<>();
		entityKey.put("participants.id",customerId);
		entityKey.put("type",BUCKS);
		return entityKey;
	}

	private static final String SELF_URI = "api/bucks";

	private static final String BUCKS = "bucks";

	@Autowired
	private HypermediaAssembler hypermediaAssembler;

}
