package io.ari.assemblers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AssemblerTest {

	@Test
	public void shouldConvertAListOfEntities() {
		List<Map<String, Object>> entitiesData = new ArrayList<Map<String, Object>>();
		Map<String, Object> entity = new HashMap<String, Object>();
		entitiesData.add(entity);

		Map<String, Object> itemDtos = createAssemblerToTest().convertEntitiesToDto(entitiesData, PAGE_SIZE_TEST, new Integer(0));

		assertNotNull("The result entities must not be null", itemDtos);
		assertTrue("Must have a items field", itemDtos.containsKey("items"));

		Collection<Map<String, Object>> items = (Collection<Map<String, Object>>) itemDtos.get("items");

		assertEquals("The size of the collection is not the expected", 1, items.size());
		assertNotSame("The entities representation is not the same of the view", items.iterator().next(), entity);
	}

	@Test
	public void shouldReturnPagingInformation() {
		List<Map<String, Object>> entitiesData = new ArrayList<Map<String, Object>>();
		entitiesData.add(new HashMap<>());
		entitiesData.add(new HashMap<>());

		Map<String, Object> itemDtos = createAssemblerToTest().convertEntitiesToDto(entitiesData, PAGE_SIZE_TEST, new Integer(0));

		assertTrue("The representation has a paging field", itemDtos.containsKey("paging"));
		assertTrue("The paging information is a map", itemDtos.get("paging") instanceof Map);
	}

	@Test
	public void shouldContainPageSize() {
		List<Map<String, Object>> entitiesData = new ArrayList<Map<String, Object>>();
		entitiesData.add(new HashMap<>());
		entitiesData.add(new HashMap<>());

		Map<String, Object> itemsDtos = createAssemblerToTest().convertEntitiesToDto(entitiesData, PAGE_SIZE_TEST, new Integer(0));
		Map<String, Object> paging = (Map<String, Object>) itemsDtos.get("paging");

		assertTrue("The paging information contains a 'page_size' field", paging.containsKey("page_size"));
		assertEquals("The page size is not the expected", PAGE_SIZE_TEST, paging.get("page_size"));
	}

	@Test
	public void shouldContainDefaultPage() {
		List<Map<String, Object>> entitiesData = new ArrayList<Map<String, Object>>();
		entitiesData.add(new HashMap<>());
		entitiesData.add(new HashMap<>());

		Map<String, Object> itemsDtos = createAssemblerToTest().convertEntitiesToDto(entitiesData, PAGE_SIZE_TEST, new Integer(0));
		Map<String, Object> paging = (Map<String, Object>) itemsDtos.get("paging");

		assertTrue("The paging information contains a 'page' field", paging.containsKey("page"));
		assertEquals("The page should have the default value", DEFAULT_PAGE_TEST, paging.get("page"));
	}

	@Test
	public void shouldHaveASelfLink() {
		List<Map<String, Object>> entitiesData = new ArrayList<Map<String, Object>>();
		entitiesData.add(new HashMap<>());
		entitiesData.add(new HashMap<>());

		Map<String, Object> itemsDtos = createAssemblerToTest().convertEntitiesToDto(entitiesData, PAGE_SIZE_TEST, new Integer(0));

		Map<String, Object> links = (Map<String, Object>) itemsDtos.get("_links");

		assertNotNull("The links must be not null", links);
	}

	private Assembler createAssemblerToTest() {
		Assembler assembler = new Assembler() {
			@Override
			public Map<String, Object> convertEntityToDto(Map<String, Object> itemData, Object... additionalData) {
				return new HashMap<>();
			}

			@Override
			protected String getCollectionSelfLink() {
				return TEST_SELF_LINK;
			}
		};

		assembler.setHypermediaAssembler(hypermediaAssembler);

		return assembler;
	}

	private static final Integer PAGE_SIZE_TEST = 1;

	private static final Integer DEFAULT_PAGE_TEST = 0;

	private static final String TEST_SELF_LINK = "/self";

	@Mock
	private HypermediaAssembler hypermediaAssembler;
}
