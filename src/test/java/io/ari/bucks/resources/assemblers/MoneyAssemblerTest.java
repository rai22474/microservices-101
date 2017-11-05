package io.ari.bucks.resources.assemblers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import io.ari.money.domain.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(MockitoJUnitRunner.class)
public class MoneyAssemblerTest {

	@Test
	public void shouldCreateMoneyFromDto() {
		Map<String, Object> moneyDto = ImmutableMap.of("value", new BigDecimal("10.2"), "currency", "EUR");

		MoneyAssembler moneyAssembler = new MoneyAssembler();
		Money money = moneyAssembler.convertDtoToEntity(moneyDto);

		assertNotNull("The money must be in dto", money);
		assertEquals("The money must not the expected", new Money(new BigDecimal("10.2"), "EUR"), money);
	}

	@Test
	public void shouldCreateDtoFromDecimalMoney() {
		Money money = new Money(new BigDecimal("10.2"), "EUR");

		Map<String, Object> moneyDto = moneyAssembler.convertEntityToDto(money);

		assertNotNull("The money must be in dto", money);
		assertEquals("The money must not the expected", ImmutableMap.of("value", new BigDecimal("10.2"), "currency", "EUR"), moneyDto);
	}

	@Test
	public void shouldCreateDtoFromIntegerMoney() {
		Money money = new Money(BigDecimal.TEN, "EUR");

		Map<String, Object> moneyDto = moneyAssembler.convertEntityToDto(money);

		assertNotNull("The money must be in dto", money);
		assertEquals("The money must not the expected", ImmutableMap.of("value", BigDecimal.TEN, "currency", "EUR"), moneyDto);
	}

	@Before
	public void prepareAssembler() {
		moneyAssembler.setObjectMapper(getObjectMapper());
	}

	private ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
		return mapper;
	}

	@InjectMocks
	private MoneyAssembler moneyAssembler;
}
