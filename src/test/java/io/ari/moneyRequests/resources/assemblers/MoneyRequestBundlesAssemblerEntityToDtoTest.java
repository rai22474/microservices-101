package io.ari.moneyRequests.resources.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.bucks.resources.assemblers.ViolationsAssembler;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyRequests.domain.MoneyRequest;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyRequestBundlesAssemblerEntityToDtoTest {

	@Test
	public void shouldBuildBundleDto() {
		MoneyRequestBundle bundle = mock(MoneyRequestBundle.class);
		String bundleId = "f76fd86f78d8f5";
		when(bundle.getId()).thenReturn(bundleId);

		Map<String, Object> bundleDto = assembler.convertEntityToDto(bundle);

		assertNotNull("Returned dto cannot be null.", bundleDto);
		assertNotNull("Returned dto must contain a links block.", bundleDto.get("_links"));
		assertNotNull("Returned dto must contain a self link.", ((Map<String, Object>) bundleDto.get("_links")).get("self"));
		Collection<Map<String, Object>> selfLinks = (Collection<Map<String, Object>>) ((Map<String, Object>) bundleDto.get("_links")).get("self");
		String selfHref = (String) selfLinks.iterator().next().get("href");
		assertTrue("Returned dto must contain self link with the bundle identifier.", selfHref.contains(bundleId));
	}

	@Test
	public void shouldHaveErrors() {
		Collection<Violation> violations = ImmutableList.of(violation);
		when(moneyRequestBundle.hasViolations()).thenReturn(Boolean.TRUE);
		when(moneyRequestBundle.getViolations()).thenReturn(violations);

		Collection<Map<String, Object>> expectedViolations = ImmutableSet.of(ImmutableMap.<String, Object>of("code", "101"));
		when(violationsAssembler.convertEntitiesToDtos(violations)).thenReturn(expectedViolations);

		when(moneyRequestBundle.getId()).thenReturn(TEST_BUNDLE_ID);

		Map<String, Object> moneyRequestBundleDto = assembler.convertEntityToDto(moneyRequestBundle);

		assertTrue("The dto must have a status item", moneyRequestBundleDto.containsKey("status"));
		assertEquals("The status must the expected", expectedViolations, moneyRequestBundleDto.get("status"));
	}

	@Test
	public void shouldNotHaveErrors() {
		when(moneyRequestBundle.hasViolations()).thenReturn(Boolean.FALSE);

		when(moneyRequestBundle.getId()).thenReturn(TEST_BUNDLE_ID);

		Map<String, Object> moneyRequestBundleDto = assembler.convertEntityToDto(moneyRequestBundle);

		assertFalse("The dto cannot have a status item", moneyRequestBundleDto.containsKey("status"));
	}

	@Test
	public void shouldHaveAListOfMoneyRequests() {
		when(moneyRequestBundle.getId()).thenReturn(TEST_BUNDLE_ID);
		when(moneyRequestBundle.getRequests()).thenReturn(ImmutableList.of(moneyRequest));

		when(moneyRequestsAssembler.convertEntityToDto(moneyRequest)).thenReturn(createMoneyRequestDto());

		Map<String, Object> moneyRequestBundleDraftDto = assembler.convertEntityToDto(moneyRequestBundle);

		Collection<Map<String, Object>> moneyRequests = (Collection<Map<String, Object>>) moneyRequestBundleDraftDto.get("moneyRequests");
		assertNotNull("The dto must have an money requests", moneyRequests);
	}

	@Test
	public void shouldHaveAnAmount() {
		Money amountMoney = new Money(new BigDecimal("10.20"), "EUR");
		when(moneyRequestBundle.calculateAmount()).thenReturn(amountMoney);
		when(moneyRequestBundle.getId()).thenReturn(TEST_BUNDLE_ID);

		when(moneyRequestsAssembler.convertEntityToDto(moneyRequest)).thenReturn(createMoneyRequestDto());
		ImmutableMap<String, Object> expectedAmount = ImmutableMap.of("value", new BigDecimal("10.20"), "currency", "EUR");
		when(moneyAssembler.convertEntityToDto(amountMoney)).thenReturn(expectedAmount);
		Map<String, Object> moneyRequestBundleDraftDto = assembler.convertEntityToDto(moneyRequestBundle);

		Map<String, Object> amount = (Map<String, Object>) moneyRequestBundleDraftDto.get("amount");
		assertNotNull("The dto must have an amount", amount);
		assertEquals("The amount is not the expected", expectedAmount, amount);
	}

	@Test
	public void shouldHaveReason() {
		when(moneyRequestBundle.getReason()).thenReturn(TEST_REASON);
		when(moneyRequestBundle.getId()).thenReturn(TEST_BUNDLE_ID);
		when(moneyRequestsAssembler.convertEntityToDto(moneyRequest)).thenReturn(createMoneyRequestDto());

		Map<String, Object> moneyRequestBundleDraftDto = assembler.convertEntityToDto(moneyRequestBundle);

		String reason = (String) moneyRequestBundleDraftDto.get("reason");
		assertNotNull("The dto must have an reason", reason);
		assertEquals("The amount is not the expected", TEST_REASON, reason);
	}

	@Before
	public void prepareCustomerBucks() {
		when(bucksRepository.findBucksByCustomerId(TEST_CUSTOMER_ID)).thenReturn(customerBucks);
	}

	private Map<String, Object> createMoneyRequestDto() {
		Map<String, Object> moneyRequest = Maps.newHashMap();

		moneyRequest.put("amount", ImmutableMap.of("value", new BigDecimal("13.4"), "currency", "EUR"));
		moneyRequest.put("to", ImmutableMap.of("mobilePhone", "+34600151248"));
		return moneyRequest;
	}

	@InjectMocks
	private MoneyRequestBundlesAssembler assembler;

	@Mock
	private MoneyRequestsAssembler moneyRequestsAssembler;

	@Mock
	private MoneyRequest request;

	@Mock
	private BucksRepository bucksRepository;

	@Mock
	private Bucks customerBucks;

	@Mock
	private MoneyRequestBundle moneyRequestBundle;

	@Mock
	private MoneyRequest moneyRequest;

	@Mock
	private Violation violation;

	@Mock
	private ViolationsAssembler violationsAssembler;

	@Mock
	private MoneyAssembler moneyAssembler;

	private static final String TEST_CUSTOMER_ID = "b7d4a7d4555ad5";

	private static final String TEST_BUNDLE_ID = "c7e4f7e4555be5";

	private static final String TEST_REASON = "reason";

}
