package io.ari.bucks.domain.repositories.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.assemblers.BucksStorageAssembler;
import io.ari.money.domain.Money;
import io.ari.repositories.assemblers.StorageAssembler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static io.ari.money.MoneyBuilder.val;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BucksStorageAssemblerConvertEntityToDtoTest {

	@Before
	public void recordBuckIdentifiers() {
		when(bucks.getBankingServiceAgreementId()).thenReturn(TEST_BANKING_SERVICE_ID);
	}

	@Test
	public void shouldReturnANotNullMap() {
		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		assertNotNull("The returned map must be not null", bucksDto);
	}

	@Test
	public void shouldHaveBalance() {
		Money available = val("100.10").eur().entity();
		when(bucks.getAvailableBalance()).thenReturn(available);

		Map<String, Object> moneyDto = createMoneyDto();
		when(moneyStorageAssembler.convertEntityToDto(available)).thenReturn(moneyDto);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		assertTrue("The returned map must contain a balance element.", bucksDto.containsKey("balance"));
	}

	@Test
	public void shouldHaveAvailableBalance() {
		Money available = val("100.10").eur().entity();
		when(bucks.getAvailableBalance()).thenReturn(available);

		Map<String, Object> moneyDto = createMoneyDto();
		when(moneyStorageAssembler.convertEntityToDto(available)).thenReturn(moneyDto);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		Map<String, Object> balance = (Map<String, Object>) bucksDto.get("balance");
		assertEquals("The returned map must have balance.available", moneyDto, balance.get("available"));
	}

	@Test
	public void shouldHaveBlockedBalance() {
		Money blocked = val("100.10").eur().entity();
		when(bucks.getBlockedBalance()).thenReturn(blocked);

		Map<String, Object> moneyDto = createMoneyDto();
		when(moneyStorageAssembler.convertEntityToDto(blocked)).thenReturn(moneyDto);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		Map<String, Object> balance = (Map<String, Object>) bucksDto.get("balance");
		assertEquals("The returned map must have balance.blocked", moneyDto, balance.get("blocked"));
	}

	@Test
	public void shouldHaveTotalBalance() {
		Money totalBalance = val("100.10").eur().entity();
		when(bucks.getTotalBalance()).thenReturn(totalBalance);

		Map<String, Object> moneyDto = createMoneyDto();
		when(moneyStorageAssembler.convertEntityToDto(totalBalance)).thenReturn(moneyDto);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		Map<String, Object> balance = (Map<String, Object>) bucksDto.get("balance");
		assertEquals("The returned map must have balance.pots", moneyDto, balance.get("total"));
	}

	@Test
	public void shouldHaveLimits() {
		Money rechargedThisPeriod = val("100.10").eur().entity();
		when(bucks.getRemainingRechargeLimit()).thenReturn(rechargedThisPeriod);

		Map<String, Object> moneyDto = createMoneyDto();
		when(moneyStorageAssembler.convertEntityToDto(rechargedThisPeriod)).thenReturn(moneyDto);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		assertTrue("The returned map must contain a limits element.", bucksDto.containsKey("limits"));
	}

	@Test
	public void shouldHaveRechargeLimits() {
		Money rechargedThisPeriod = val("100.10").eur().entity();
		when(bucks.getRemainingRechargeLimit()).thenReturn(rechargedThisPeriod);

		Map<String, Object> moneyDto = createMoneyDto();
		when(moneyStorageAssembler.convertEntityToDto(rechargedThisPeriod)).thenReturn(moneyDto);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		Map<String, Object> limits = (Map<String, Object>) bucksDto.get("limits");
		assertTrue("The returned map must contain a limits.recharge element.", limits.containsKey("recharge"));
	}

	@Test
	public void shouldHaveRemainingRechargeLimit() {
		Money rechargedThisPeriod = val("100.10").eur().entity();
		when(bucks.getRemainingRechargeLimit()).thenReturn(rechargedThisPeriod);

		Map<String, Object> moneyDto = createMoneyDto();
		when(moneyStorageAssembler.convertEntityToDto(rechargedThisPeriod)).thenReturn(moneyDto);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		Map<String, Object> limits = (Map<String, Object>) bucksDto.get("limits");
		Map<String, Object> rechargeLimits = (Map<String, Object>) limits.get("recharge");
		assertEquals("The returned map must contain the expected limits.recharge.remaining.", moneyDto, rechargeLimits.get("remaining"));
	}

	@Test
	public void shouldHaveLastRechargeLimit() {
		Money lastRechargeLimit = val("100.10").eur().entity();
		when(bucks.getLastRecharge()).thenReturn(lastRechargeLimit);

		Map<String, Object> moneyDto = createMoneyDto();
		when(moneyStorageAssembler.convertEntityToDto(lastRechargeLimit)).thenReturn(moneyDto);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		Map<String, Object> limits = (Map<String, Object>) bucksDto.get("limits");
		Map<String, Object> rechargeLimits = (Map<String, Object>) limits.get("recharge");
		assertEquals("The returned map must contain the expected limits.recharge.last.", moneyDto, rechargeLimits.get("last"));
	}

	@Test
	public void shouldHaveMaxRechargeLimit() {
		Money maxRechargeLimit = val("100.10").eur().entity();
		when(bucks.getMaxRechargeLimit()).thenReturn(maxRechargeLimit);

		Map<String, Object> moneyDto = createMoneyDto();
		when(moneyStorageAssembler.convertEntityToDto(maxRechargeLimit)).thenReturn(moneyDto);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		Map<String, Object> limits = (Map<String, Object>) bucksDto.get("limits");
		Map<String, Object> rechargeLimits = (Map<String, Object>) limits.get("recharge");
		assertEquals("The returned map must contain the expected limits.recharge.max.", moneyDto, rechargeLimits.get("max"));
	}

	@Test
	public void shouldHaveThisPeriodRechargeLimit() {
		Money thisPeriodRechargeLimit = val("100.10").eur().entity();
		when(bucks.getThisPeriodRechargeLimit()).thenReturn(thisPeriodRechargeLimit);

		Map<String, Object> moneyDto = createMoneyDto();
		when(moneyStorageAssembler.convertEntityToDto(thisPeriodRechargeLimit)).thenReturn(moneyDto);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		Map<String, Object> limits = (Map<String, Object>) bucksDto.get("limits");
		Map<String, Object> rechargeLimits = (Map<String, Object>) limits.get("recharge");
		assertEquals("The returned map must contain the expected limits.recharge.thisPeriod.", moneyDto, rechargeLimits.get("thisPeriod"));
	}
	
	@Test
	public void shouldHaveDaysTillMaxLimit() {
		when(bucks.getDaysTillMaxLimit()).thenReturn(365);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		Map<String, Object> limits = (Map<String, Object>) bucksDto.get("limits");
		assertEquals("The returned map must contain the expected limits.recharge.max.", new Integer("365"), limits.get("daysTillMaxLimit"));
	}

	@Test
	public void shouldHaveABlockedBalances() {
		Map<String, Money> blockedBalances = newHashMap();
		Money money = val("100.10").eur().entity();
		blockedBalances.put("1", money);

		Map<String, Object> blockedBalancesDto = newHashMap();
		Map<String, Object> moneyDto = createMoneyDto();
		blockedBalancesDto.put("1", moneyDto);

		when(bucks.getBlockedBalances()).thenReturn(blockedBalances);
		when(moneyStorageAssembler.convertEntityToDto(money)).thenReturn(moneyDto);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		assertNotNull("The returned map must have a blocked balances object", bucksDto.get("blockedBalances"));
		assertEquals("The map must be the expected", blockedBalancesDto, bucksDto.get("blockedBalances"));
	}

	@Test
	public void shouldHaveABankingServiceIdentifier() {
		when(bucks.getBankingServiceAgreementId()).thenReturn(TEST_BANKING_SERVICE_ID);

		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		assertNotNull("The returned map must have banking services agreement object", bucksDto.get("bankingServiceAgreementId"));
		assertEquals("The identifier must be the expected", TEST_BANKING_SERVICE_ID, bucksDto.get("bankingServiceAgreementId"));
	}

	@Test
	public void shouldHaveCustomerId() {
		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		assertTrue("The returned map must contain a participants element.", bucksDto.containsKey("participants"));
		Map<String, Object> participant = ((List<Map<String, Object>>) bucksDto.get("participants")).stream().findFirst().get();
		assertTrue("The returned map must contain a participant with an id.", participant.containsKey("id"));
		assertEquals("The returned map must contain the expected participant.", CUSTOMER_ID, participant.get("id"));
	}

	@Test
	public void shouldHaveBucksType() {
		Map<String, Object> bucksDto = assembler.convertEntityToDto(bucks);

		assertTrue("The returned map must have a type.", bucksDto.containsKey("type"));
		assertEquals("The dto must have the right type", "bucks", bucksDto.get("type"));
	}

	@Before
	public void setupBucksCustomer() {
		when(bucks.getCustomerId()).thenReturn(CUSTOMER_ID);
	}

	private ImmutableMap<String, Object> createMoneyDto() {
		return ImmutableMap.of("value", val("100.10").eur().entity());
	}

	@InjectMocks
	private BucksStorageAssembler assembler;

	@Mock
	private StorageAssembler<Money> moneyStorageAssembler;

	@Mock
	private Bucks bucks;

	private static final String TEST_BANKING_SERVICE_ID = "608c7f79cdf86cd7194f6c0e";

	private static final String CUSTOMER_ID = "g9a86fa795fa84fa9f0a8";
}
