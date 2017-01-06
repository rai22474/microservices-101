package io.ari.moneyRequests.domain.repositories.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.moneyOrders.domain.recipients.repositories.assemblers.RecipientsStorageAssembler;
import io.ari.moneyRequests.domain.MoneyRequest;
import io.ari.repositories.assemblers.StorageAssembler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MoneyRequestsStorageAssembler.class)
public class MoneyRequestsStorageAssemblerDtoToEntityTest {

	@Test
	public void shouldNotReturnNull() {
		MoneyRequest returnedMoneyRequest = moneyRequestsStorageAssembler.convertDtoToEntity(moneyRequestDto);

		assertNotNull("The money requests cannot be null.", returnedMoneyRequest);
	}

	@Test
	public void shouldCreateMoneyRequestWithId() {
		MoneyRequest returnedMoneyRequest = moneyRequestsStorageAssembler.convertDtoToEntity(moneyRequestDto);

		assertEquals("Returned money request must be the expected.", moneyRequest, returnedMoneyRequest);
	}

	@Test
	public void shouldCreateMoneyRequestWithStatus() {
		MoneyRequest returnedMoneyRequest = moneyRequestsStorageAssembler.convertDtoToEntity(moneyRequestDto);

		assertEquals("Returned money request must be the expected.", moneyRequest, returnedMoneyRequest);
	}

	@Test
	public void shouldHaveAnStatusOfTheMoneyRequest() {
		MoneyRequest returnedMoneyRequest = moneyRequestsStorageAssembler.convertDtoToEntity(moneyRequestDto);

		verify(returnedMoneyRequest).setStatus("submitted");
	}

	@Test
	public void shouldHaveAnAmountOfTheMoneyRequest() {
		Money money = mock(Money.class);
		when(moneyStorageAssembler.convertDtoToEntity((Map<String, Object>) moneyRequestDto.get("amount"))).thenReturn(money);

		MoneyRequest returnedMoneyRequest = moneyRequestsStorageAssembler.convertDtoToEntity(moneyRequestDto);

		verify(returnedMoneyRequest).setAmount(money);
	}

	@Test
	public void shouldHaveRecipientData() {
		Recipient recipient = mock(Recipient.class);
		Map<String, Object> recipientData = ImmutableMap.of("email", "anEmail@aDomain.com", "name", "my dear friend");
		when(partiesStorageAssembler.convertDtoToEntity(recipientData)).thenReturn(recipient);

		MoneyRequest returnedMoneyRequest = moneyRequestsStorageAssembler.convertDtoToEntity(moneyRequestDto);

		verify(returnedMoneyRequest).setRecipient(recipient);
	}

	@Test
	public void shouldSetBucksId() {
		MoneyRequest returnedMoneyRequest = moneyRequestsStorageAssembler.convertDtoToEntity(moneyRequestDto);

		verify(returnedMoneyRequest).setBucksId(BUCKS_ID);
	}

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Before
	public void prepareMoneyRequest() throws Exception {
		whenNew(MoneyRequest.class).withArguments(ID).thenReturn(moneyRequest);
	}

	@InjectMocks
	private MoneyRequestsStorageAssembler moneyRequestsStorageAssembler;

	@Mock
	private MoneyRequest moneyRequest;

	@Mock
	private StorageAssembler<Money> moneyStorageAssembler;

	@Mock
	private RecipientsStorageAssembler partiesStorageAssembler;

	private static final String ID = "da96d9a75da9";

	private static final String BUCKS_ID = "bf97a697fa967f";

	private static Map<String, Object> moneyRequestDto = newHashMap();

	static {
		moneyRequestDto.put("id", ID);
		moneyRequestDto.put("amount", ImmutableMap.of(
				"value", "10.0",
				"currency", "EUR"));
		moneyRequestDto.put("bucksId", BUCKS_ID);
		moneyRequestDto.put("email", "anEmail@aDomain.com");
		moneyRequestDto.put("name", "my dear friend");
		moneyRequestDto.put("status", "submitted");
	}

}
