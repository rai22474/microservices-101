package io.ari.moneyRequests.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.bucks.resources.assemblers.RecipientsAssembler;
import io.ari.bucks.resources.assemblers.ViolationsAssembler;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.moneyRequests.domain.MoneyRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyRequestsAssemblerDtoToEntityTest {

	@Test
	public void shouldNotReturnNull() {
		when(moneyAssembler
				.convertDtoToEntity(ImmutableMap.of("value", new BigDecimal("13.4"), "currency", "EUR")))
				.thenReturn(new Money(new BigDecimal("13.4"), "EUR"));

		MoneyRequest moneyRequest = moneyRequestsAssembler.convertDtoToEntity(createMoneyRequestDto(), BUCKS_ID);

		assertNotNull("The money request must be not null", moneyRequest);
	}

	@Test
	public void shouldHaveTheCorrectAmount() {
		when(moneyAssembler
				.convertDtoToEntity(ImmutableMap.of("value", new BigDecimal("13.4"), "currency", "EUR")))
				.thenReturn(new Money(new BigDecimal("13.4"), "EUR"));

		MoneyRequest moneyRequest = moneyRequestsAssembler.convertDtoToEntity(createMoneyRequestDto(), BUCKS_ID);

		assertEquals("The money request amount must be equals", new Money(new BigDecimal("13.4"), "EUR"), moneyRequest.getAmount());
	}

	@Test
	public void shouldHaveAUniqueIdentifier() {
		MoneyRequest moneyRequest = moneyRequestsAssembler.convertDtoToEntity(createMoneyRequestDto(), BUCKS_ID);

		assertNotNull("The money request have an unique identifier", moneyRequest.getId());
	}

	@Test
	public void shouldHaveARecipient() {
		MoneyRequest moneyRequest = moneyRequestsAssembler.convertDtoToEntity(createMoneyRequestDto(), BUCKS_ID);

		assertNotNull("The money request must have a recipient", moneyRequest.getRecipient());
		assertEquals("The money request must have the expected party as the recipient.", party, moneyRequest.getRecipient());
	}

	@Test
	public void shouldHaveBucksId() {
		MoneyRequest moneyRequest = moneyRequestsAssembler.convertDtoToEntity(createMoneyRequestDto(), BUCKS_ID);

		assertNotNull("The money request must have a bucksId", moneyRequest.getBucksId());
		assertEquals("The money request must have the expected bucksId as the recipient.", BUCKS_ID, moneyRequest.getBucksId());
	}

	@Before
	public void setupContactsAssembler() {
		when(partiesAssembler.convertDtoToEntity(ImmutableMap.of("mobilePhone", MOBILE_PHONE))).thenReturn(party);
	}

	private Map<String, Object> createMoneyRequestDto() {
		Map<String,Object> dto = newHashMap();
		
		dto.put("amount", ImmutableMap.of("value", new BigDecimal("13.4"), "currency", "EUR"));
		dto.put("mobilePhone", MOBILE_PHONE);
		dto.put("status","submitted");
		
		return ImmutableMap.copyOf(dto);
	}

	@InjectMocks
	private MoneyRequestsAssembler moneyRequestsAssembler;

	@Mock
	private MoneyAssembler moneyAssembler;

	@Mock
	private RecipientsAssembler partiesAssembler;

	@Mock
	private Recipient party;

	@Mock
	private MoneyRequest moneyRequest;

	@Mock
	private ViolationsAssembler violationsAssembler;

	private static final String MOBILE_PHONE = "600151248";

	private static final String BUCKS_ID = "d9ab8ada8d58a4c";

}
