package io.ari.moneyRequests.resources.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.ari.assemblers.HypermediaAssembler;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
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
public class MoneyRequestBundleDraftsAssemblerTest {

	@Test
	public void shouldCreateAMoneyRequestValidDraftBundle() {
		Map<String, Object> moneyRequestBundleDraftDto = createMoneyRequestBundle(createMoneyRequest());
		when(moneyRequestBundlesAssembler.convertDtoToEntity(TEST_CUSTOMER_ID, moneyRequestBundleDraftDto)).thenReturn(moneyRequestBundle);

		MoneyRequestBundle moneyRequestBundleDraft = moneyRequestBundleDraftsAssembler.convertDtoToEntity(TEST_CUSTOMER_ID, moneyRequestBundleDraftDto);

		assertNotNull("The request draft must be not null", moneyRequestBundleDraft);
		verify(moneyRequestBundleDraft).setStatus("draft");
	}

	@Test
	public void shouldCreateADraftDto() {
		Map<String, Object> moneyRequestBundleDto = createMoneyRequestBundle(createMoneyRequestDto());
		when(moneyRequestBundlesAssembler.convertEntityToDto(moneyRequestBundle)).thenReturn(moneyRequestBundleDto);

		Map<String, Object> draftDto = moneyRequestBundleDraftsAssembler.convertEntityToDto(moneyRequestBundle);

		moneyRequestBundleDto.forEach((key, value) ->
				assertEquals("The draft dto must contain all bundle dto keys.", value, draftDto.get(key)));
	}

	@Test
	public void shouldCreateADraftDtoWithLinks() {
		Map<String, Object> moneyRequestBundleDto = createMoneyRequestBundle(createMoneyRequestDto());
		when(moneyRequestBundlesAssembler.convertEntityToDto(moneyRequestBundle)).thenReturn(moneyRequestBundleDto);

		Map<String, Object> draftDto = moneyRequestBundleDraftsAssembler.convertEntityToDto(moneyRequestBundle);

		assertTrue("The draft dto must have a _links item", draftDto.containsKey("_links"));
	}

	@Test
	public void shouldCreateADraftDtoWithEditLink() {
		Map<String, Object> draftDto = moneyRequestBundleDraftsAssembler.convertEntityToDto(moneyRequestBundle);

		Map<String, Object> links = (Map<String, Object>) draftDto.get("_links");
		assertTrue("The draft dto links must contain a moneyRequestDraft.edit link.", links.containsKey("editMoneyRequestDraft"));
	}

	private Map<String, Object> createMoneyRequest() {
		Map<String, Object> moneyRequest = Maps.newHashMap();

		moneyRequest.put("amount", ImmutableMap.of("value", new BigDecimal("13.4"), "currency", "EUR"));
		moneyRequest.put("to", ImmutableMap.of("mobilePhone", "+34600151248"));

		return moneyRequest;
	}

	private Map<String, Object> createMoneyRequestBundle(Map<String, Object> moneyRequest) {
		return ImmutableMap.of("moneyRequests", ImmutableList.of(moneyRequest), "reason", "Children payment");
	}

	private Map<String, Object> createMoneyRequestDto() {
		Map<String, Object> moneyRequest = Maps.newHashMap();

		moneyRequest.put("amount", ImmutableMap.of("value", new BigDecimal("13.4"), "currency", "EUR"));
		moneyRequest.put("to", ImmutableMap.of("mobilePhone", "+34600151248"));
		return moneyRequest;
	}

	@InjectMocks
	private MoneyRequestBundleDraftsAssembler moneyRequestBundleDraftsAssembler;

	@Mock
	private MoneyRequestBundlesAssembler moneyRequestBundlesAssembler;

	@Mock
	private MoneyRequestsAssembler moneyRequestsAssembler;

	@Mock
	private MoneyRequestBundle moneyRequestBundle;

	@Mock
	private HypermediaAssembler hypermediaAssembler;


	private static final String TEST_CUSTOMER_ID = "b7d4a7d4555ad5";
}
