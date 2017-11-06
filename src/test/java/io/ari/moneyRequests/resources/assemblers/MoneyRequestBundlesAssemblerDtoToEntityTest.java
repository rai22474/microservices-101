package io.ari.moneyRequests.resources.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.bucks.resources.assemblers.ViolationsAssembler;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyRequests.domain.MoneyRequest;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
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

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyRequestBundlesAssemblerDtoToEntityTest {

	@Test
	public void shouldBuildBundleAndNotReturnNull() throws Exception {
		Map<String, Object> moneyRequestDto = createMoneyRequestDto();
		Map<String, Object> moneyRequestBundleDto = createMoneyRequestBundle(moneyRequestDto);
		when(moneyRequestsAssembler.convertDtoToEntity(moneyRequestDto, BUCKS_ID)).thenReturn(request);

		MoneyRequestBundle bundle = assembler.convertDtoToEntity(CUSTOMER_ID, moneyRequestBundleDto);

		assertNotNull("Returned bundle cannot be null.", bundle);
	}

	@Test
	public void shouldAssignId() {
		Map<String, Object> moneyRequest = createMoneyRequestDto();
		Map<String, Object> moneyRequestBundle = createMoneyRequestBundle(moneyRequest);
		when(moneyRequestsAssembler.convertDtoToEntity(moneyRequest, BUCKS_ID)).thenReturn(request);

		MoneyRequestBundle bundle = assembler.convertDtoToEntity(CUSTOMER_ID, moneyRequestBundle);

		assertNotNull("Returned bundle must have an id.", bundle.getId());
	}

	@Test
	public void shouldAssignCreationDate() {
		Map<String, Object> moneyRequest = createMoneyRequestDto();
		Map<String, Object> moneyRequestBundle = createMoneyRequestBundle(moneyRequest);
		when(moneyRequestsAssembler.convertDtoToEntity(moneyRequest, BUCKS_ID)).thenReturn(request);

		MoneyRequestBundle bundle = assembler.convertDtoToEntity(CUSTOMER_ID, moneyRequestBundle);

		assertNotNull("Returned bundle must have a creation date.", bundle.getCreationDate());
	}

	@Test
	public void shouldAssignBucksId() {
		Map<String, Object> moneyRequest = createMoneyRequestDto();
		Map<String, Object> moneyRequestBundle = createMoneyRequestBundle(moneyRequest);
		when(moneyRequestsAssembler.convertDtoToEntity(moneyRequest, BUCKS_ID)).thenReturn(request);

		MoneyRequestBundle bundle = assembler.convertDtoToEntity(CUSTOMER_ID, moneyRequestBundle);

		assertNotNull("The bundle must have the customer bucks", bundle.getBucksId());
		assertEquals("The bucks must be equals to the expected", BUCKS_ID, bundle.getBucksId());
	}

	@Test
	public void shouldAssignRequests() {
		Map<String, Object> moneyRequest = createMoneyRequestDto();
		Map<String, Object> moneyRequestBundle = createMoneyRequestBundle(moneyRequest);
		when(moneyRequestsAssembler.convertDtoToEntity(moneyRequest, BUCKS_ID)).thenReturn(request);

		MoneyRequestBundle bundle = assembler.convertDtoToEntity(CUSTOMER_ID, moneyRequestBundle);

		assertNotNull("Returned bundle requests cannot be null.", bundle.getRequests());
		assertFalse("Returned bundle requests cannot be empty.", bundle.getRequests().isEmpty());
		assertEquals("It must be returned only one request.", 1, bundle.getRequests().size());
		assertEquals("The returned request must be the expected one.", request, bundle.getRequests().stream().findFirst().get());
	}

	@Test
	public void shouldAssignReason() {
		Map<String, Object> moneyRequest = createMoneyRequestDto();
		Map<String, Object> moneyRequestBundle = createMoneyRequestBundle(moneyRequest);
		when(moneyRequestsAssembler.convertDtoToEntity(moneyRequest, BUCKS_ID)).thenReturn(request);

		when(request.getAmount()).thenReturn(new Money(new BigDecimal("10"), "EUR"));

		MoneyRequestBundle bundle = assembler.convertDtoToEntity(CUSTOMER_ID, moneyRequestBundle);

		assertNotNull("Returned bundle requests cannot be null.", bundle.getRequests());
		assertNotNull("Returned bundle must contain a reason.", bundle.getReason());
		assertEquals("Returned bundle reason must be the expected.", "Children payment", bundle.getReason());
	}

	@Test
	public void shouldNotAssignReason() {
		MoneyRequestBundle bundle = assembler.convertDtoToEntity(CUSTOMER_ID, createMoneyRequestWithoutReason());

		assertNull("Returned bundle cannot contain a reason.", bundle.getReason());
	}

	@Before
	public void prepareCustomerBucks() {
		when(bucksRepository.findBucksByCustomerId(CUSTOMER_ID)).thenReturn(customerBucks);
		when(bucksRepository.findById(BUCKS_ID)).thenReturn(customerBucks);
		when(customerBucks.getId()).thenReturn(BUCKS_ID);
	}
	
	@Before
	public void prepareTimeService(){
		when(timeServer.currentDate()).thenReturn(new Date());
	}

	private Map<String, Object> createMoneyRequestWithoutReason() {
		Map<String, Object> moneyRequest = createMoneyRequestDto();
		Map<String, Object> bundleDto = newHashMap(createMoneyRequestBundle(moneyRequest));

		bundleDto.remove("reason");

		return bundleDto;
	}

	private Map<String, Object> createMoneyRequestBundle(Map<String, Object> moneyRequest) {
		return ImmutableMap.of("moneyRequests", ImmutableList.of(moneyRequest), "reason", "Children payment");
	}

	private Map<String, Object> createMoneyRequestDto() {
		Map<String, Object> moneyRequest = newHashMap();

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
	private Violation violation;

	@Mock
	private ViolationsAssembler violationsAssembler;

	@Mock
	private MoneyAssembler moneyAssembler;
	
	@Mock
	private TimeServer timeServer;

	private static final String CUSTOMER_ID = "b7d4a7d4555ad5";

	private static final String BUCKS_ID = "b5c4f6e3455be5";
}
