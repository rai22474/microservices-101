package io.ari.bucks.domain.repositories.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.assemblers.BucksStorageAssembler;
import io.ari.money.domain.Money;
import io.ari.repositories.assemblers.StorageAssembler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static io.ari.money.MoneyBuilder.val;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BucksStorageAssemblerConvertDtoToEntityTest {

	@Test
	public void shouldReturnANotNullBucks() {
		Bucks bucks = assembler.convertDtoToEntity(createBucksData());
		assertNotNull("The bucks must be not null", bucks);
	}

	@Test
	public void shouldReturnBucksWithId() {
		Bucks bucks = assembler.convertDtoToEntity(createBucksData());
		assertEquals("The bucks must be have id", BANKING_SERVICE_AGREEMENT_ID, bucks.getId());
	}

	@Test
	public void shouldReturnBucksWithAvailableBalance() {
		Money expectedMoney = val("100.00").eur().entity();
		when(moneyStorageAssembler
				.convertDtoToEntity(val("200.10").eur().dto()))
				.thenReturn(val("200.10").eur().entity());

		when(moneyStorageAssembler
				.convertDtoToEntity(val("100.10").eur().dto()))
				.thenReturn(val("100.10").eur().entity());

		Bucks bucks = assembler.convertDtoToEntity(createBucksData());

		assertEquals("The bucks must have avaibleBalance", expectedMoney, bucks.getAvailableBalance());
	}

	@Test
	public void shouldReturnBucksWithBlockedBalance() {
		Money expectedMoney = val("100.10").eur().entity();
		when(moneyStorageAssembler
				.convertDtoToEntity(val("100.10").eur().dto()))
				.thenReturn(expectedMoney);

		Bucks bucks = assembler.convertDtoToEntity(createBucksData());

		assertEquals("The bucks must have blockedBalance", expectedMoney, bucks.getBlockedBalance());
	}

	@Test
	public void shouldReturnBucksWithTotalBalance() {
		Money convertedMoney = val("200.10").eur().entity();
		Money expectedMoney = val("200.10").eur().entity();

		when(moneyStorageAssembler.convertDtoToEntity(val("200.10").eur().dto())).thenReturn(convertedMoney);

		Bucks bucks = assembler.convertDtoToEntity(createBucksData());

		assertEquals("The bucks must have totalBalance", expectedMoney, bucks.getTotalBalance());
	}

	@Test
	public void shouldReturnBucksWithDaysTillMaxLimit() {
		Bucks bucks = assembler.convertDtoToEntity(createBucksData());

		assertEquals("The bucks must have daysTillMaxLimit", new Integer("365"), bucks.getDaysTillMaxLimit());
	}

	@Test
	public void shouldReturnBucksWithRemainingRechargeLimit() {
		Money expectedMoney = val("100.10").eur().entity();
		when(moneyStorageAssembler
				.convertDtoToEntity(val("100.10").eur().dto()))
				.thenReturn(expectedMoney);

		when(moneyStorageAssembler
				.convertDtoToEntity(val("2500.00").eur().dto()))
				.thenReturn(val("2500.00").eur().entity());

		Bucks bucks = assembler.convertDtoToEntity(createBucksData());

		assertEquals("The bucks must have remainingRechargeLimit", val("2399.90").eur().entity(), bucks.getRemainingRechargeLimit());
	}

	@Test
	public void shouldReturnBucksWithLastRechargeLimit() {
		Money expectedMoney = val("100.10").eur().entity();
		when(moneyStorageAssembler
				.convertDtoToEntity(val("100.10").eur().dto()))
				.thenReturn(expectedMoney);

		Bucks bucks = assembler.convertDtoToEntity(createBucksData());

		assertEquals("The bucks must have lastRechargeLimit", expectedMoney, bucks.getLastRecharge());
	}

	@Test
	public void shouldReturnBucksWithMaxRechargeLimit() {
		Money expectedMoney = val("2500.00").eur().entity();
		when(moneyStorageAssembler
				.convertDtoToEntity(val("2500.00").eur().dto()))
				.thenReturn(expectedMoney);

		Bucks bucks = assembler.convertDtoToEntity(createBucksData());

		assertEquals("The bucks must have maxRechargeLimit", expectedMoney, bucks.getMaxRechargeLimit());
	}

	@Test
	public void shouldReturnBucksWithThisPeriodRechargeLimit() {
		Money expectedMoney = val("100.10").eur().entity();
		when(moneyStorageAssembler
				.convertDtoToEntity(val("100.10").eur().dto()))
				.thenReturn(expectedMoney);

		Bucks bucks = assembler.convertDtoToEntity(createBucksData());

		assertEquals("The bucks must have thisPeriodRechargeLimit", expectedMoney, bucks.getThisPeriodRechargeLimit());
	}

	@Test
	public void shouldHaveAMapOfBlockedBalances() {
		Money expectedMoney = val("100.10").eur().entity();

		Map<String, Object> expectedBlockedBalances = newHashMap();
		expectedBlockedBalances.put("1", expectedMoney);

		when(moneyStorageAssembler.convertDtoToEntity(val("100.10").eur().dto()))
				.thenReturn(expectedMoney);

		Bucks bucks = assembler.convertDtoToEntity(createBucksData());

		assertNotNull("The blocked balances must be not null", bucks.getBlockedBalances());
		assertEquals("The blocked balances must be the expected", expectedBlockedBalances, bucks.getBlockedBalances());
	}

	@Test
	public void shouldHaveABankingServiceAgreementId() {
		Bucks bucks = assembler.convertDtoToEntity(createBucksData());

		assertNotNull("The banking services agreement id must be not null", bucks.getBankingServiceAgreementId());
		assertEquals("banking services agreement id must be the expected", BANKING_SERVICE_AGREEMENT_ID, bucks.getBankingServiceAgreementId());
	}

	private Map<String, Object> createBucksData() {
		Map<String, Object> bucksData = newHashMap();

		bucksData.put("balance", ImmutableMap.of(
				"available", val("100.10").eur().dto(),
				"blocked", val("100.10").eur().dto(),
				"pots", val("100.10").eur().dto(),
				"coinJars", val("100.10").eur().dto(),
				"total", val("200.10").eur().dto()));
		bucksData.put("limits", ImmutableMap.of(
				"recharge", ImmutableMap.of(
						"remaining", val("100.10").eur().dto(),
						"max", val("2500.00").eur().dto(),
						"last", val("100.10").eur().dto(),
						"thisPeriod", val("100.10").eur().dto()),
					"daysTillMaxLimit", 365));
		bucksData.put("blockedBalances", createBlockedBalances());
		bucksData.put("bankingServiceAgreementId", BANKING_SERVICE_AGREEMENT_ID);
		bucksData.put("participants", ImmutableList.of(ImmutableMap.of("id", CUSTOMER_ID)));

		return ImmutableMap.copyOf(bucksData);
	}

	private Map<String, Object> createBlockedBalances() {
		return ImmutableMap.of("1", val("100.10").eur().dto());
	}

	@InjectMocks
	private BucksStorageAssembler assembler;

	@Mock
	private StorageAssembler<Money> moneyStorageAssembler;

	private static final String BANKING_SERVICE_AGREEMENT_ID = "608c7f79cdf86cd7194f6c0e";

	private static final String CUSTOMER_ID = "as8d7a9s8da9s87d";
}
