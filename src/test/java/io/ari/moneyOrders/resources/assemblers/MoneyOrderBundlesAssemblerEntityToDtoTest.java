package io.ari.moneyOrders.resources.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.ari.assemblers.HypermediaAssembler;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.bucks.resources.assemblers.ViolationsAssembler;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.repositories.exceptions.EntityNotFound;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyOrderBundlesAssemblerEntityToDtoTest {

	@Test
	public void shouldNotReturnNull() {
		MoneyOrderBundle bundle = mock(MoneyOrderBundle.class);
		String bundleId = "f76fd86f78d8f5";
		when(bundle.getId()).thenReturn(bundleId);

		Map<String, Object> bundleDto = assembler.convertEntityToDto(bundle);

		assertNotNull("Returned dto cannot be null.", bundleDto);
	}

	@Test
	public void shouldReturnSelfLink() {
		MoneyOrderBundle bundle = mock(MoneyOrderBundle.class);
		String bundleId = "f76fd86f78d8f5";
		when(bundle.getId()).thenReturn(bundleId);

		Map<String, Object> expectedLinks = ImmutableMap.of("self", "daojsdoa");
		when(hypermediaAssembler.createHypermedia("api/commands/" + bundleId, "ari-read")).thenReturn(expectedLinks);

		Map<String, Object> bundleDto = assembler.convertEntityToDto(bundle);

		assertNotNull("Returned dto must contain a links block.", bundleDto.get("_links"));
		assertEquals("Returned dto must contain the expected self link.", expectedLinks, bundleDto.get("_links"));
	}


	@Test
	public void shouldHaveAListOfMoneyOrders() {
		when(moneyOrderBundle.getId()).thenReturn(BUNDLE_ID);
		when(moneyOrderBundle.getOrders()).thenReturn(ImmutableList.of(moneyOrder));

		when(moneyOrdersAssembler.convertEntityToDto(moneyOrder)).thenReturn(createMoneyOrderDto());

		Map<String, Object> moneyOrderBundleDraftDto = assembler.convertEntityToDto(moneyOrderBundle);

		Collection<Map<String, Object>> moneyOrders = (Collection<Map<String, Object>>) moneyOrderBundleDraftDto.get("moneyOrders");
		assertNotNull("The dto must have an money orders", moneyOrders);
	}

	@Test
	public void shouldHaveAnAmount() {
		Money amountMoney = new Money(new BigDecimal("10.20"), "EUR");
		when(moneyOrderBundle.calculateAmount()).thenReturn(amountMoney);
		when(moneyOrderBundle.getId()).thenReturn(BUNDLE_ID);

		when(moneyOrdersAssembler.convertEntityToDto(moneyOrder)).thenReturn(createMoneyOrderDto());
		ImmutableMap<String, Object> expectedAmount = ImmutableMap.of("value", new BigDecimal("10.20"), "currency", "EUR");
		when(moneyAssembler.convertEntityToDto(amountMoney)).thenReturn(expectedAmount);
		Map<String, Object> moneyOrderBundleDraftDto = assembler.convertEntityToDto(moneyOrderBundle);

		Map<String, Object> amount = (Map<String, Object>) moneyOrderBundleDraftDto.get("amount");
		assertNotNull("The dto must have an amount", amount);
		assertEquals("The amount is not the expected", expectedAmount, amount);
	}

	@Test
	public void shouldHaveReason() {
		when(moneyOrderBundle.getReason()).thenReturn(REASON);
		when(moneyOrderBundle.getId()).thenReturn(BUNDLE_ID);
		when(moneyOrdersAssembler.convertEntityToDto(moneyOrder)).thenReturn(createMoneyOrderDto());

		Map<String, Object> moneyOrderBundleDraftDto = assembler.convertEntityToDto(moneyOrderBundle);

		String reason = (String) moneyOrderBundleDraftDto.get("reason");
		assertNotNull("The dto must have a reason", reason);
		assertEquals("The reason is not the expected", REASON, reason);
	}

	@Test
	public void shouldHaveErrors() {
		Violation violation = mock(Violation.class);
		ImmutableSet<Violation> violations = ImmutableSet.of(violation);
		when(moneyOrderBundle.hasViolations()).thenReturn(Boolean.TRUE);
		when(moneyOrderBundle.getViolations()).thenReturn(violations);

		Collection<Map<String, Object>> expectedErrors = ImmutableSet.of(ImmutableMap.<String, Object>of("code", "101"));
		when(violationsAssembler.convertEntitiesToDtos(violations)).thenReturn(expectedErrors);

		when(moneyOrderBundle.getId()).thenReturn(BUNDLE_ID);
		when(moneyOrdersAssembler.convertEntityToDto(moneyOrder)).thenReturn(createMoneyOrderDto());

		Map<String, Object> moneyOrderBundleDto = assembler.convertEntityToDto(moneyOrderBundle);

		assertTrue("The dto must have a status item.", moneyOrderBundleDto.containsKey("status"));
		assertEquals("The dto must have the right status item.", expectedErrors, moneyOrderBundleDto.get("status"));
	}

	@Test
	public void shouldNotHaveErrors() {
		when(moneyOrderBundle.hasViolations()).thenReturn(Boolean.FALSE);
		when(moneyOrderBundle.getViolations()).thenReturn(ImmutableSet.of());

		when(moneyOrderBundle.getId()).thenReturn(BUNDLE_ID);
		when(moneyOrdersAssembler.convertEntityToDto(moneyOrder)).thenReturn(createMoneyOrderDto());

		Map<String, Object> moneyOrderBundleDraftDto = assembler.convertEntityToDto(moneyOrderBundle);

		assertFalse("The dto must have an errors item.", moneyOrderBundleDraftDto.containsKey("errors"));
	}

	@Before
	public void prepareCustomerBucks() throws EntityNotFound {
		when(bucksRepository.findBucksByCustomerId(CUSTOMER_ID)).thenReturn(customerBucks);
	}

	private Map<String, Object> createMoneyOrderDto() {
		Map<String, Object> moneyOrder = newHashMap();

		moneyOrder.put("amount", ImmutableMap.of("value", new BigDecimal("13.4"), "currency", "EUR"));
		moneyOrder.put("to", ImmutableMap.of("mobilePhone", "+34600151248"));
		return moneyOrder;
	}

	@InjectMocks
	private MoneyOrderBundlesAssembler assembler;

	@Mock
	private MoneyOrdersAssembler moneyOrdersAssembler;

	@Mock
	private MoneyOrder order;

	@Mock
	private BucksRepository bucksRepository;

	@Mock
	private Bucks customerBucks;

	@Mock
	private MoneyOrderBundle moneyOrderBundle;

	@Mock
	private MoneyOrder moneyOrder;

	@Mock
	private Violation violation;

	@Mock
	private ViolationsAssembler violationsAssembler;

	@Mock
	private MoneyAssembler moneyAssembler;

	@Mock
	private HypermediaAssembler hypermediaAssembler;

	private static final String CUSTOMER_ID = "b7d4a7d4555ad5";

	private static final String BUNDLE_ID = "c7e4f7e4555be5";

	private static final String REASON = "reason";
}
