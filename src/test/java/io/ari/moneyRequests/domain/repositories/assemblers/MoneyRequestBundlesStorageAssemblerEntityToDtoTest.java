package io.ari.moneyRequests.domain.repositories.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.ari.money.domain.Money;
import io.ari.moneyRequests.domain.MoneyRequest;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.repositories.assemblers.StorageAssembler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyRequestBundlesStorageAssemblerEntityToDtoTest {


	@Test
	public void shouldReturnNotNullWithSerializeMoneyRequestBundle() {
		Map<String, Object> moneyRequestBundleDto = moneyRequestBundlesStorageAssembler.convertEntityToDto(moneyRequestBundle);

		assertNotNull("The money request must be not null", moneyRequestBundleDto);
	}

	@Test
	public void shouldHasAListOfSerializedMoneyRequest() {
		Collection<MoneyRequest> moneyRequests = ImmutableList.of(firstMoneyRequest, secondMoneyRequest);
		when(moneyRequestBundle.getRequests()).thenReturn(moneyRequests);

		Map<String, Object> firstMoneyRequestDto = createMoneyRequest("34.2");

		when(moneyRequestStorageAssembler.convertEntityToDto(firstMoneyRequest)).thenReturn(firstMoneyRequestDto);
		when(moneyRequestStorageAssembler.convertEntityToDto(secondMoneyRequest)).thenReturn(createMoneyRequest("44.2"));

		Map<String, Object> moneyRequestBundleDto = moneyRequestBundlesStorageAssembler.convertEntityToDto(moneyRequestBundle);

		Collection<Map<String, Object>> moneyRequestDtos = (Collection<Map<String, Object>>) moneyRequestBundleDto.get("moneyRequests");
		assertNotNull("There must have a list of money requests", moneyRequestDtos);
		assertEquals("The number of request must be the expected", 2, moneyRequestDtos.size());

		assertEquals("The money request is not the expected", firstMoneyRequestDto, moneyRequestDtos.stream().findFirst().get());
	}

	@Test
	public void shouldGenerateADtoWithAnAgree() {
		Map<String, Object> dto = moneyRequestBundlesStorageAssembler.convertEntityToDto(moneyRequestBundle);

		assertTrue("The dto must have an agree.", dto.containsKey("agree"));
		assertEquals("The dto agree must be the bucks identifier.", BUCKS_ID, dto.get("agree"));
	}

	@Test
	public void shouldGenerateADtoWithAReason() {
		Map<String, Object> dto = moneyRequestBundlesStorageAssembler.convertEntityToDto(moneyRequestBundle);

		assertTrue("The dto must have an reason.", dto.containsKey("reason"));
		assertEquals("The dto reason must be the expected.", REASON, dto.get("reason"));
	}

	@Test
	public void shouldGenerateADtoWithoutAReason() {
		when(moneyRequestBundle.getReason()).thenReturn(null);

		Map<String, Object> dto = moneyRequestBundlesStorageAssembler.convertEntityToDto(moneyRequestBundle);

		assertFalse("The dto must not have an reason.", dto.containsKey("reason"));
	}

	@Test
	public void shouldGenerateADtoWithASourceCommand() {
		Map<String, Object> dto = moneyRequestBundlesStorageAssembler.convertEntityToDto(moneyRequestBundle);

		assertTrue("The dto must have an source command.", dto.containsKey("sourceCommand"));
		assertEquals("The dto source command must be the expected.", SOURCE_COMMAND, dto.get("sourceCommand"));
	}

	@Test
	public void shouldGenerateADtoWithoutASourceCommand() {
		when(moneyRequestBundle.getSourceCommand()).thenReturn(null);

		Map<String, Object> dto = moneyRequestBundlesStorageAssembler.convertEntityToDto(moneyRequestBundle);

		assertFalse("The dto must not have an source command.", dto.containsKey("sourceCommand"));
	}
	
	@Test
	public void shouldGenerateADtoWithAmount() {
		Map<String, Object> dto = moneyRequestBundlesStorageAssembler.convertEntityToDto(moneyRequestBundle);

		Object amount = dto.get("amount");
		assertNotNull("The dto must not have an amount.", amount);
	}

	@Test
	public void shouldGenerateADtoWithId() {
		Map<String, Object> dto = moneyRequestBundlesStorageAssembler.convertEntityToDto(moneyRequestBundle);

		assertTrue("The dto must have an id.", dto.containsKey("id"));
		assertEquals("The dto id must be the expected.", ID, dto.get("id"));
	}

	@Test
	public void shouldGenerateADtoWithAStatus() {
		Map<String, Object> dto = moneyRequestBundlesStorageAssembler.convertEntityToDto(moneyRequestBundle);

		assertTrue("The dto must have an status.", dto.containsKey("status"));
		assertEquals("The dto id must be the expected.", STATUS, dto.get("status"));
	}
	
	@Test
	public void shouldGenerateADtoWithCreationDate() {
		Map<String, Object> dto = moneyRequestBundlesStorageAssembler.convertEntityToDto(moneyRequestBundle);

		assertTrue("The dto must have a creationDate.", dto.containsKey("creationDate"));
		assertEquals("The dto creationDate must be the expected.", CREATION_DATE, dto.get("creationDate"));
	}

	@Before
	public void prepareBundle() {
		when(moneyStorageAssembler.convertEntityToDto(money)).thenReturn(ImmutableMap.of("value", new BigDecimal("10.0"), "currency", "EUR"));

		when(moneyRequestBundle.getBucksId()).thenReturn(BUCKS_ID);
		when(moneyRequestBundle.getReason()).thenReturn(REASON);
		when(moneyRequestBundle.calculateAmount()).thenReturn(money);
		when(moneyRequestBundle.getCreationDate()).thenReturn(CREATION_DATE);
		when(moneyRequestBundle.getId()).thenReturn(ID);
		when(moneyRequestBundle.getStatus()).thenReturn(STATUS);
		when(moneyRequestBundle.getSourceCommand()).thenReturn(SOURCE_COMMAND);
	}

	private Map<String, Object> createMoneyRequest(String amount) {
		Map<String, Object> moneyRequest = Maps.newHashMap();

		moneyRequest.put("amount", ImmutableMap.of("value", new BigDecimal(amount), "currency", "EUR"));
		moneyRequest.put("to", ImmutableMap.of("mobilePhone", "+34600151248"));
		return moneyRequest;
	}

	@InjectMocks
	private MoneyRequestBundlesStorageAssembler moneyRequestBundlesStorageAssembler;

	@Mock
	private MoneyRequestBundle moneyRequestBundle;

	@Mock(name = "firstMoneyRequest")
	private MoneyRequest firstMoneyRequest;

	@Mock(name = "secondMoneyRequest")
	private MoneyRequest secondMoneyRequest;

	@Mock
	private Money money;

	@Mock
	private StorageAssembler<Money> moneyStorageAssembler;

	private static final String BUCKS_ID = "fda84d6ad468ad7";

	private static final String REASON = "some mundane reason";
	
	private static final String SOURCE_COMMAND = "asidja0sdj0sdf8sdh";

	private static final Date CREATION_DATE = new Date();

	private static final String ID = "asidja0sdj0sdf8sdh";

	private static final String STATUS = "submitted";
	
	@Mock
	private MoneyRequestsStorageAssembler moneyRequestStorageAssembler;

}
