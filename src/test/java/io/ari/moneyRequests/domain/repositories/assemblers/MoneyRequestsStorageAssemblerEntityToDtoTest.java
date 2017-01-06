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
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyRequestsStorageAssemblerEntityToDtoTest {

	@Test
	public void shouldReturnNotNullWhenConvertMoneyRequest() {
		Map<String, Object> moneyRequestDto = moneyRequestsStorageAssembler.convertEntityToDto(moneyRequest);

		assertNotNull("The money request assembler is not null", moneyRequestDto);
	}

	@Test
	public void shouldHaveTheRecipientDetails() {
		Recipient party = mock(Recipient.class);
		when(moneyRequest.getRecipient()).thenReturn(party);

		String email = "anEmail@anDomain.com";
		Map<String, Object> partyData = ImmutableMap.of("email", email);
		when(partiesStorageAssembler.convertEntityToDto(party)).thenReturn(partyData);

		Map<String, Object> moneyRequestDto = moneyRequestsStorageAssembler.convertEntityToDto(moneyRequest);

		assertNotNull("The moneyRequest must have an email.", moneyRequestDto.get("email"));
		assertEquals("The moneyRequest email must be the expected.", email, moneyRequestDto.get("email"));
	}

	@Test
	public void shouldHaveAnAmountOfTheMoneyRequest() {
		when(moneyRequest.getAmount()).thenReturn(new Money(new BigDecimal("20.0"), "EUR"));
		Map<String, Object> moneyRequestDto = moneyRequestsStorageAssembler.convertEntityToDto(moneyRequest);

		assertNotNull("The moneyRequest must have an amount", moneyRequestDto.get("amount"));
	}

	@Test
	public void shouldHaveAnStatusOfTheMoneyRequest() {
		Map<String, Object> moneyRequestDto = moneyRequestsStorageAssembler.convertEntityToDto(moneyRequest);

		assertNotNull("The moneyRequest must have an status", moneyRequestDto.get("status"));
	}

	@Test
	public void shouldHaveAnIdOfTheMoneyRequest() {
		Map<String, Object> moneyRequestDto = moneyRequestsStorageAssembler.convertEntityToDto(moneyRequest);

		assertNotNull("The moneyRequest must have an id", moneyRequestDto.get("id"));
	}

	@Test
	public void shouldHaveBucksId() {
		Map<String, Object> moneyRequestDto = moneyRequestsStorageAssembler.convertEntityToDto(moneyRequest);

		assertTrue("The dto must have a bucksId", moneyRequestDto.containsKey("bucksId"));
		assertEquals("The dto bucksId must be the expected.", BUCKS_ID, moneyRequestDto.get("bucksId"));
	}

	@Before
	public void initMoneyRequest() {
		when(moneyRequest.getStatus()).thenReturn("submitted");
		when(moneyRequest.getId()).thenReturn("fda84d6ad468ad7");
		when(moneyRequest.getBucksId()).thenReturn(BUCKS_ID);
	}

	@InjectMocks
	private MoneyRequestsStorageAssembler moneyRequestsStorageAssembler;

	@Mock
	private MoneyRequest moneyRequest;

	@Mock
	private StorageAssembler<Money> moneyStorageAssembler;

	@Mock
	private RecipientsStorageAssembler partiesStorageAssembler;

	private static final String BUCKS_ID = "f08a6f08a068f";

}
