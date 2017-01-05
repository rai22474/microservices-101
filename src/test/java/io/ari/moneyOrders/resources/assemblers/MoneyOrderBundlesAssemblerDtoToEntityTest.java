package io.ari.moneyOrders.resources.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.bucks.resources.assemblers.ViolationsAssembler;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.repositories.exceptions.EntityNotFound;
import io.ari.time.TimeServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyOrderBundlesAssemblerDtoToEntityTest {

	@Test
	public void shouldNotReturnNull() throws Exception {
		Map<String, Object> moneyOrder = createMoneyOrderDto();
		Map<String, Object> moneyOrderBundle = createMoneyOrderBundle(moneyOrder);

		when(moneyOrdersAssembler.convertDtoToEntity(moneyOrderBundle, TEST_BUCK_ID)).thenReturn(order);

		MoneyOrderBundle bundle = assembler.convertDtoToEntity(TEST_CUSTOMER_ID, moneyOrderBundle);

		assertNotNull("Returned bundle cannot be null.", bundle);
	}

	@Test
	public void shouldAssignNewId() throws EntityNotFound {
		Map<String, Object> moneyOrder = createMoneyOrderDto();
		Map<String, Object> moneyOrderBundle = createMoneyOrderBundle(moneyOrder);
		when(moneyOrdersAssembler.convertDtoToEntity(moneyOrderBundle, TEST_BUCK_ID)).thenReturn(order);

		MoneyOrderBundle bundle = assembler.convertDtoToEntity(TEST_CUSTOMER_ID, createMoneyOrderBundle(moneyOrderBundle));

		assertNotNull("The bundle must have an id", bundle.getId());
	}

	@Test
	public void shouldAssignGivenId() throws EntityNotFound {
		Map<String, Object> moneyOrder = createMoneyOrderDto();
		Map<String, Object> moneyOrderBundle = createMoneyOrderBundle(moneyOrder);
		when(moneyOrdersAssembler.convertDtoToEntity(moneyOrderBundle, TEST_BUCK_ID)).thenReturn(order);

		String moneyOrderBundleId = "anExistingIdentifier";

		MoneyOrderBundle bundle = assembler.convertDtoToExistingEntity(TEST_CUSTOMER_ID, moneyOrderBundleId, createMoneyOrderBundle(moneyOrderBundle));

		assertNotNull("The bundle must have an id", bundle.getId());
		assertEquals("The bundle must have the given identifier", moneyOrderBundleId, bundle.getId());
	}

	@Test
	public void shouldAssignBucks() throws EntityNotFound {
		Map<String, Object> moneyOrder = createMoneyOrderDto();
		Map<String, Object> moneyOrderBundle = createMoneyOrderBundle(moneyOrder);
		when(moneyOrdersAssembler.convertDtoToEntity(moneyOrderBundle, TEST_BUCK_ID)).thenReturn(order);

		MoneyOrderBundle bundle = assembler.convertDtoToEntity(TEST_CUSTOMER_ID, createMoneyOrderBundle(moneyOrderBundle));

		assertNotNull("The bundle must have the customer bucks", bundle.getBucksId());
		assertEquals("The bucks must be equals to the expected", TEST_BUCK_ID, bundle.getBucksId());
	}

	@Test
	public void shouldAssignMoneyOrders() throws EntityNotFound {
		Map<String, Object> moneyOrder = createMoneyOrderDto();
		Map<String, Object> moneyOrderBundle = createMoneyOrderBundle(moneyOrder);
		when(moneyOrdersAssembler.convertDtoToEntity(moneyOrderBundle, TEST_BUCK_ID)).thenReturn(order);

		MoneyOrderBundle bundle = assembler.convertDtoToEntity(TEST_CUSTOMER_ID, createMoneyOrderBundle(moneyOrderBundle));

		assertNotNull("Returned bundle orders cannot be null.", bundle.getOrders());
		assertFalse("Returned bundle orders cannot be empty.", bundle.getOrders().isEmpty());
		assertEquals("It must be returned only one order.", 1, bundle.getOrders().size());
		assertEquals("The returned order must be the expected one.", order, bundle.getOrders().stream().findFirst().get());
	}

	@Test
	public void shouldAssignReason() throws EntityNotFound {
		Map<String, Object> moneyOrder = createMoneyOrderDto();
		Map<String, Object> moneyOrderBundle = createMoneyOrderBundle(moneyOrder);

		when(moneyOrdersAssembler.convertDtoToEntity(moneyOrderBundle, TEST_BUCK_ID)).thenReturn(order);
		when(order.getAmount()).thenReturn(new Money(new BigDecimal("10"), "EUR"));

		MoneyOrderBundle bundle = assembler.convertDtoToEntity(TEST_CUSTOMER_ID, createMoneyOrderBundle(moneyOrderBundle));

		assertNotNull("Returned bundle orders cannot be null.", bundle.getOrders());

		assertNotNull("Returned bundle must contain a reason.", bundle.getReason());
		assertEquals("Returned bundle reason must be the expected.", "Children payment", bundle.getReason());
	}

	@Test
	public void shouldNotAssignReason() throws EntityNotFound {
		Map<String, Object> moneyOrder = createMoneyOrderDto();
		when(moneyOrdersAssembler.convertDtoToEntity(moneyOrder, TEST_BUCK_ID)).thenReturn(order);
		when(order.getAmount()).thenReturn(new Money(new BigDecimal("10"), "EUR"));

		MoneyOrderBundle bundle = assembler.convertDtoToEntity(TEST_CUSTOMER_ID, createMoneyOrderWithoutReason());

		assertNull("Returned bundle cannot contain a reason.", bundle.getReason());
	}

	@Test
	public void shouldAssignCreationDate() throws EntityNotFound {
		Map<String, Object> moneyOrder = createMoneyOrderDto();
		when(moneyOrdersAssembler.convertDtoToEntity(moneyOrder, TEST_BUCK_ID)).thenReturn(order);
		when(order.getAmount()).thenReturn(new Money(new BigDecimal("10"), "EUR"));

		MoneyOrderBundle bundle = assembler.convertDtoToEntity(TEST_CUSTOMER_ID, createMoneyOrderBundle(moneyOrder));

		assertNotNull("Returned bundle creation date cannot be null.", bundle.getCreationDate());
	}

	@Before
	public void prepareCustomerBucks() throws EntityNotFound {
		when(bucksRepository.findBucksByCustomerId(TEST_CUSTOMER_ID)).thenReturn(customerBucks);

		when(bucksRepository.findById(TEST_BUCK_ID)).thenReturn(customerBucks);
		when(customerBucks.getId()).thenReturn(TEST_BUCK_ID);
	}

	@Before
	public void recordTimeService(){
		when(timeServer.currentDate()).thenReturn(new Date());
	}
	
	private Map<String, Object> createMoneyOrderWithoutReason() {
		Map<String, Object> moneyOrder = createMoneyOrderDto();
		Map<String, Object> bundleDto = Maps.newHashMap(createMoneyOrderBundle(moneyOrder));

		bundleDto.remove("reason");

		return bundleDto;
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
	private TimeServer timeServer;

	private static final String TEST_CUSTOMER_ID = "b7d4a7d4555ad5";

	private static final String TEST_BUCK_ID = "b5c4f6e3455be5";
}
