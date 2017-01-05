package io.ari.moneyOrders.resources.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.ari.assemblers.HypermediaAssembler;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.repositories.exceptions.EntityNotFound;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyOrderBundleDraftsAssemblerTest {

	@Test
	public void shouldCreateANewValidMoneyOrderBundleDraft() throws EntityNotFound {
		Map<String, Object> moneyOrderBundleDraftDto = createMoneyOrderBundle(createMoneyOrder());
		when(moneyOrderBundlesAssembler.convertDtoToEntity(CUSTOMER_ID, moneyOrderBundleDraftDto)).thenReturn(moneyOrderBundle);

		MoneyOrderBundle returnedEntity = moneyOrderBundleDraftsAssembler.convertDtoToEntity(CUSTOMER_ID, moneyOrderBundleDraftDto);

		assertNotNull("The returned draft cannot be null", returnedEntity);
		assertEquals("The returned draft must be the expected.", moneyOrderBundle, returnedEntity);
		verify(returnedEntity).setStatus("draft");
	}

	@Test
	public void shouldCreateAnExistingMoneyOrderBundleDraft() throws EntityNotFound {
		Map<String, Object> moneyOrderBundleDraftDto = createMoneyOrderBundle(createMoneyOrder());
		when(moneyOrderBundlesAssembler.convertDtoToExistingEntity(CUSTOMER_ID, DRAFT_ID, moneyOrderBundleDraftDto)).thenReturn(moneyOrderBundle);

		MoneyOrderBundle returnedEntity = moneyOrderBundleDraftsAssembler.convertDtoToExistingEntity(CUSTOMER_ID, DRAFT_ID, moneyOrderBundleDraftDto);

		assertNotNull("The returned draft cannot be null", returnedEntity);
		assertEquals("The returned draft must be the expected.", moneyOrderBundle, returnedEntity);
		verify(returnedEntity).setStatus("draft");
	}

	@Test
	public void shouldCreateADraftDto() {
		Map<String, Object> moneyOrderBundleDto = createMoneyOrderBundle(createMoneyOrderDto());
		when(moneyOrderBundlesAssembler.convertEntityToDto(moneyOrderBundle)).thenReturn(moneyOrderBundleDto);

		Map<String, Object> draftDto = moneyOrderBundleDraftsAssembler.convertEntityToDto(moneyOrderBundle);

		moneyOrderBundleDto.forEach((key, value) ->
				assertEquals("The draft dto must contain all bundle dto keys.", value, draftDto.get(key)));
	}

	@Test
	public void shouldCreateADraftDtoWithLinks() {
		Map<String, Object> moneyOrderBundleDto = createMoneyOrderBundle(createMoneyOrderDto());
		when(moneyOrderBundlesAssembler.convertEntityToDto(moneyOrderBundle)).thenReturn(moneyOrderBundleDto);

		Map<String, Object> draftDto = moneyOrderBundleDraftsAssembler.convertEntityToDto(moneyOrderBundle);

		assertTrue("The draft dto must have a _links item", draftDto.containsKey("_links"));
	}

	@Test
	public void shouldCreateADraftDtoWithEditLink() {
		Map<String, Object> draftDto = moneyOrderBundleDraftsAssembler.convertEntityToDto(moneyOrderBundle);

		Map<String, Object> links = (Map<String, Object>) draftDto.get("_links");
		assertTrue("The draft dto links must contain a editMoneyOrderDraft link.", links.containsKey("editMoneyOrderDraft"));
	}

	private Map<String, Object> createMoneyOrder() {
		Map<String, Object> moneyOrder = Maps.newHashMap();

		moneyOrder.put("amount", ImmutableMap.of("value", new BigDecimal("13.4"), "currency", "EUR"));
		moneyOrder.put("to", ImmutableMap.of("mobilePhone", "+34600151248"));

		return moneyOrder;
	}

	private Map<String, Object> createMoneyOrderBundle(Map<String, Object> moneyOrder) {
		return ImmutableMap.of("moneyOrders", ImmutableList.of(moneyOrder), "reason", "Children payment");
	}

	private Map<String, Object> createMoneyOrderDto() {
		Map<String, Object> moneyOrder = Maps.newHashMap();

		moneyOrder.put("amount", ImmutableMap.of("value", new BigDecimal("13.4"), "currency", "EUR"));
		moneyOrder.put("to", ImmutableMap.of("mobilePhone", "+34600151248"));
		return moneyOrder;
	}

	@InjectMocks
	private MoneyOrderBundleDraftsAssembler moneyOrderBundleDraftsAssembler;

	@Mock
	private MoneyOrderBundlesAssembler moneyOrderBundlesAssembler;

	@Mock
	private MoneyOrdersAssembler moneyOrdersAssembler;

	@Mock
	private MoneyOrderBundle moneyOrderBundle;

	@Mock
	private HypermediaAssembler hypermediaAssembler;

	private static final String CUSTOMER_ID = "b7d4a7d4555ad5";

	private static final String DRAFT_ID = "faosjfiasodfj93f923";
}
