package io.ari.moneyOrders.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.bucks.resources.assemblers.RecipientsAssembler;
import io.ari.bucks.resources.assemblers.ViolationsAssembler;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.MoneyOrder;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.uidGenerator.UIDGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyOrdersAssemblerDtoToEntityTest {

	@Test
	public void shouldNotReturnNull() {
		when(moneyAssembler
				.convertDtoToEntity(ImmutableMap.of("value", new BigDecimal("13.4"), "currency", "EUR")))
				.thenReturn(new Money(new BigDecimal("13.4"), "EUR"));

		MoneyOrder moneyOrder = moneyOrdersAssembler.convertDtoToEntity(createMoneyOrderDto(),TEST_BUCK_ID);

		assertNotNull("The money order must be not null", moneyOrder);
	}

	@Test
	public void shouldHaveTheCorrectAmount() {
		when(moneyAssembler
				.convertDtoToEntity(ImmutableMap.of("value", new BigDecimal("13.4"), "currency", "EUR")))
				.thenReturn(new Money(new BigDecimal("13.4"), "EUR"));

		MoneyOrder moneyOrder = moneyOrdersAssembler.convertDtoToEntity(createMoneyOrderDto(),TEST_BUCK_ID);

		assertEquals("The money order amount must be equals", new Money(new BigDecimal("13.4"), "EUR"), moneyOrder.getAmount());
	}

	@Test
	public void shouldHaveAUniqueIdentifier() {
		MoneyOrder moneyOrder = moneyOrdersAssembler.convertDtoToEntity(createMoneyOrderDto(),TEST_BUCK_ID);

		assertNotNull("The money order have an unique identifier", moneyOrder.getId());
	}

	@Test
	public void shouldHaveARecipient() {
		MoneyOrder moneyOrder = moneyOrdersAssembler.convertDtoToEntity(createMoneyOrderDto(),TEST_BUCK_ID);

		assertNotNull("The money order must have a recipient", moneyOrder.getRecipient());
		assertEquals("The money order must have the expected party as the recipient.", party, moneyOrder.getRecipient());
	}

	@Test
	public void shouldHaveABucksId() {
		MoneyOrder moneyOrder = moneyOrdersAssembler.convertDtoToEntity(createMoneyOrderDto(),TEST_BUCK_ID);

		assertNotNull("The money order must have a bucks id", moneyOrder.getBucksId());
		assertEquals("The money order must have the expected .", TEST_BUCK_ID, moneyOrder.getBucksId());
	}
	
	@Before
	public void setupContactsAssembler() {
		when(uidGenerator.generateUID()).thenReturn(UID);
		when(partiesAssembler.convertDtoToEntity(ImmutableMap.of("mobilePhone", TEST_CONTACT_PHONE))).thenReturn(party);
	}

	private Map<String, Object> createMoneyOrderDto() {
		return ImmutableMap.of(
				"amount", ImmutableMap.of("value", new BigDecimal("13.4"), "currency", "EUR"),
				"mobilePhone", TEST_CONTACT_PHONE);
	}

	@InjectMocks
	private MoneyOrdersAssembler moneyOrdersAssembler;

	@Mock
	private MoneyAssembler moneyAssembler;

	@Mock
	private RecipientsAssembler partiesAssembler;

	@Mock
	private Recipient party;

	@Mock
	private MoneyOrder moneyOrder;

	@Mock
	private ViolationsAssembler violationsAssembler;
	
	@Mock
	private UIDGenerator uidGenerator;

	private static final String TEST_CONTACT_PHONE = "600151248";
	
	private static final String TEST_BUCK_ID = "b5c4f6e3455be5";

	private static final String UID = "1234567abcde";
}
