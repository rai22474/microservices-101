package io.ari.assemblers;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Assembler {

	public Map<String, Object> convertEntitiesToDto(List<Map<String, Object>> entitiesJson, Integer pageSize, Integer page, Object... additionalData) {
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();

		boolean moreElements = entitiesJson.size() > pageSize;

		if (entitiesJson.size() != 0 && moreElements) {
			entitiesJson.remove(entitiesJson.size() - 1);
		}

		for (Map<String, Object> entityJson : entitiesJson) {
			items.add(convertEntityToDto(entityJson, additionalData));
		}

		Map<String, Object> dtosJson = new HashMap<>();
		dtosJson.put("items", items);

		if (moreElements) {
			dtosJson.put("paging", createPagingInformation(pageSize, page, moreElements));
		}

		dtosJson.put("_links", hypermediaAssembler.createHypermedia(getCollectionSelfLink(),"ari-read"));

		return dtosJson;
	}

	protected abstract String getCollectionSelfLink();

	public abstract Map<String, Object> convertEntityToDto(Map<String, Object> entityData, Object... additionalData);

	private Map<String, Object> createPagingInformation(Integer pageSize, Integer page, boolean moreElements) {
		Map<String, Object> paging = new HashMap<>();
		paging.put("page_size", pageSize);
		paging.put("more_pages", moreElements);
		paging.put("page", page);

		return paging;
	}

	void setHypermediaAssembler(HypermediaAssembler hypermediaAssembler) {
		this.hypermediaAssembler = hypermediaAssembler;
	}

	@Autowired
	private HypermediaAssembler hypermediaAssembler;
}