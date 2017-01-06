package io.ari.moneyRequests.resources.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.bucks.resources.assemblers.RecipientsAssembler;
import io.ari.bucks.resources.assemblers.ViolationsAssembler;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.businessRules.OrderRecipientIsNotTheSender;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.moneyRequests.domain.MoneyRequest;
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
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyRequestsAssemblerEntityToDtoTest {

	@Test
	public void shouldReturnANotNullDto() {
		Map<String, Object> moneyRequestDto = moneyRequestsAssembler.convertEntityToDto(moneyRequest);

		assertNotNull("The money request must be not null", moneyRequestDto);
	}

	@Test
	public void shouldReturnAnAmount() {
		Money amount = new Money(new BigDecimal("10.29"), "EUR");
		when(moneyRequest.getAmount()).thenReturn(amount);
		Map<String, Object> moneyDto = ImmutableMap.of("value", new BigDecimal("10.29"), "currency", "EUR");
		when(moneyAssembler.convertEntityToDto(amount)).thenReturn(moneyDto);

		Map<String, Object> moneyRequestDto = moneyRequestsAssembler.convertEntityToDto(moneyRequest);

		assertNotNull("The money request must have an amount", moneyRequestDto.get("amount"));
		assertEquals("The amount is not the expected", moneyDto, moneyRequestDto.get("amount"));
	}

	@Test
	public void shouldReturnAnId() {
		Map<String, Object> moneyRequestDto = moneyRequestsAssembler.convertEntityToDto(moneyRequest);

		assertTrue("Returned dto must contain an id.", moneyRequestDto.containsKey("id"));
		assertEquals("Returned dto must contain the right id.", MONEY_REQUEST_ID, moneyRequestDto.get("id"));
	}

	@Test
	public void shouldReturnRecipient() {
		when(moneyRequest.getRecipient()).thenReturn(recipient);

		Map<String, Object> recipientDto = createNoWizzoContact();
		when(partiesAssembler.convertEntityToDto(recipient)).thenReturn(recipientDto);

		Map<String, Object> moneyRequestDto = moneyRequestsAssembler.convertEntityToDto(moneyRequest);

		recipientDto.keySet().stream()
				.forEach(key -> assertNotNull("The money request must have " + key, moneyRequestDto.get(key)));
	}

	@Test
	public void shouldReturnErrors() {
		Violation unfilteredViolation = createViolation(OrderRecipientIsNotTheSender.ERROR_CODE, "a message");
		Collection<Violation> violations = ImmutableList.of(
				unfilteredViolation,
				createViolation("RecipientIsBlocked", "An arcane message"));
		when(validator.validate(moneyRequest)).thenReturn(violations);

		ImmutableList<Map<String, Object>> violationsDto = ImmutableList.of();
		when(violationsAssembler.convertEntitiesToDtos(any(Collection.class))).thenReturn(violationsDto);

		Map<String, Object> moneyRequestDto = moneyRequestsAssembler.convertEntityToDto(moneyRequest);

		assertNotNull("The money request must be have status", moneyRequestDto.get("status"));
		assertEquals("The status is not the expected", violationsDto, moneyRequestDto.get("status"));
	}

	@Test
	public void shouldNotReturnErrors() {
		when(validator.validate(moneyRequest)).thenReturn(ImmutableList.of());

		Map<String, Object> moneyRequestDto = moneyRequestsAssembler.convertEntityToDto(moneyRequest);

		assertFalse("The money request cannot have a status", moneyRequestDto.containsKey("status"));
	}

	@Before
	public void setupMoneyRequest() {
		when(moneyRequest.getId()).thenReturn(MONEY_REQUEST_ID);
	}

	private Violation createViolation(String code, String message) {
		Violation violation = mock(Violation.class);
		when(violation.getCode()).thenReturn(code);
		when(violation.getMessage()).thenReturn(message);
		return violation;
	}

	private Map<String, Object> createNoWizzoContact() {
		return ImmutableMap.of("name", "Tippy",
				"lastName", "Tap",
				"mobilePhone", "600151248",
				"avatar", "potatoe");
	}

	@InjectMocks
	private MoneyRequestsAssembler moneyRequestsAssembler;

	@Mock
	private MoneyAssembler moneyAssembler;

	@Mock
	private Recipient recipient;

	@Mock
	private MoneyRequest moneyRequest;

	@Mock
	private ViolationsAssembler violationsAssembler;

	@Mock
	private RecipientsAssembler partiesAssembler;

	private static final String MONEY_REQUEST_ID = "ad9fad8f7a80s";

	@Mock
	private BusinessRulesValidator<MoneyRequest> validator;

}
