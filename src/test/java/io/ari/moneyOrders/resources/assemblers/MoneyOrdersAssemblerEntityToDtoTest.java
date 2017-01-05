package io.ari.moneyOrders.resources.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.bucks.resources.assemblers.RecipientsAssembler;
import io.ari.bucks.resources.assemblers.ViolationsAssembler;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.businessRules.OrderRecipientIsNotTheSender;
import io.ari.moneyOrders.domain.recipients.Recipient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyOrdersAssemblerEntityToDtoTest {

	@Before
	public void createDefaultMoneyOrderStub() {
		when(moneyOrder.getId()).thenReturn(EXPECTED_ID);
	}

	@Test
	public void shouldReturnANotNullDto() {
		Map<String, Object> moneyOrderDto = moneyOrdersAssembler.convertEntityToDto(moneyOrder);

		assertNotNull("The money order must be not null", moneyOrderDto);
	}

	@Test
	public void shouldReturnAnId() {
		Map<String, Object> moneyOrderDto = moneyOrdersAssembler.convertEntityToDto(moneyOrder);

		assertNotNull("The money order must have an id", moneyOrderDto.get("id"));
		assertEquals("The id is not the expected", EXPECTED_ID, moneyOrderDto.get("id"));
	}

	@Test
	public void shouldReturnAnAmount() {
		Money amount = new Money(new BigDecimal("10.29"), "EUR");
		when(moneyOrder.getAmount()).thenReturn(amount);
		Map<String, Object> moneyDto = ImmutableMap.of("value", new BigDecimal("10.29"), "currency", "EUR");
		when(moneyAssembler.convertEntityToDto(amount)).thenReturn(moneyDto);

		Map<String, Object> moneyOrderDto = moneyOrdersAssembler.convertEntityToDto(moneyOrder);

		assertNotNull("The money order must have an amount", moneyOrderDto.get("amount"));
		assertEquals("The amount is not the expected", moneyDto, moneyOrderDto.get("amount"));
	}

	@Test
	public void shouldReturnRecipient() {
		when(moneyOrder.getRecipient()).thenReturn(recipient);

		Map<String, Object> recipientDto = createNoWizzoContact();
		when(partiesAssembler.convertEntityToDto(recipient)).thenReturn(recipientDto);

		Map<String, Object> moneyOrderDto = moneyOrdersAssembler.convertEntityToDto(moneyOrder);

		recipientDto.keySet().stream()
				.forEach(key -> assertNotNull("The money order must have " + key, moneyOrderDto.get(key)));
	}

	@Test
	public void shouldReturnFilteredErrors() {
		Violation unfilteredViolation = createViolation(OrderRecipientIsNotTheSender.ERROR_CODE, "a message");
		List<Violation> violations = ImmutableList.of(
				unfilteredViolation,
				createViolation("RecipientIsBlocked", "An arcane message"),
				createViolation("NotEnoughRechargeLimit", "Another message"));
		when(businessRulesValidator.validate(moneyOrder)).thenReturn(violations);

		List<Map<String, Object>> violationsDto = ImmutableList.of(ImmutableMap.<String, Object>of("expected", "violations"));
		when(violationsAssembler.convertEntitiesToDtos(any(Collection.class))).thenReturn(violationsDto);
		/*when(violationsAssembler.convertEntitiesToDtos(argThat(allOf(
				contains(unfilteredViolation),
				contains(new ViolationWithCode("RecipientCannotReceive")))))).thenReturn(violationsDto);*/

		Map<String, Object> moneyOrderDto = moneyOrdersAssembler.convertEntityToDto(moneyOrder);

		assertNotNull("The money order must have status", moneyOrderDto.get("status"));
		assertEquals("The status is not the expected", violationsDto, moneyOrderDto.get("status"));
	}

	@Test
	public void shouldNotReturnErrors() {
		when(businessRulesValidator.validate(moneyOrder)).thenReturn(ImmutableList.of());

		Map<String, Object> moneyOrderDto = moneyOrdersAssembler.convertEntityToDto(moneyOrder);

		assertFalse("The money order cannot have a status", moneyOrderDto.containsKey("status"));
	}

	private Violation createViolation(String code, String message) {
		Violation violation = mock(Violation.class);
		when(violation.getCode()).thenReturn(code);
		when(violation.getMessage()).thenReturn(message);
		return violation;
	}

	private Map<String, Object> createNoWizzoContact() {
		return ImmutableMap.of(
				"name", "Tippy",
				"lastName", "Tap",
				"mobilePhone", "600151248",
				"avatar", "potatoe");
	}

	@InjectMocks
	private MoneyOrdersAssembler moneyOrdersAssembler;

	@Mock
	private MoneyAssembler moneyAssembler;

	@Mock
	private Recipient recipient;

	@Mock
	private MoneyOrder moneyOrder;

	@Mock
	private ViolationsAssembler violationsAssembler;

	@Mock
	private RecipientsAssembler partiesAssembler;

	private static final String EXPECTED_ID = "121badcedf";

	@Mock
	private BusinessRulesValidator<MoneyOrder> businessRulesValidator;

}
