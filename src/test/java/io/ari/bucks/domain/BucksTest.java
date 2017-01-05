package io.ari.bucks.domain;

import com.google.common.collect.ImmutableSet;

import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static io.ari.money.MoneyBuilder.val;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BucksTest {

	@Test
	public void shouldAssignCustomerIdAndBankingServiceAgreementId() {
		assertNotNull("Returned customerId cannot be null.", bucks.getCustomerId());
		assertEquals("Returned customerId must be the expected.", CUSTOMER_ID, bucks.getCustomerId());
		assertNotNull("Returned bankingServiceAgreementId cannot be null.", bucks.getBankingServiceAgreementId());
		assertEquals("Returned bankingServiceAgreementId must be the expected.", AGREEMENT_ID, bucks.getBankingServiceAgreementId());
	}

	@Test
	public void  shouldChangeTheLastRechargeLimitWhenReceiveMoney() {
		Money amountToReceive = val("100.10").eur().entity();
		bucks.receiveMoney(amountToReceive);
		
		assertEquals("The last recharge is not the expected.", val("100.10").eur().entity(), bucks.getLastRecharge());
	}

	@Test
	public void shouldDecreaseRemainingRechargeLimitWhenReceiveMoney() {
		Money amountToReceive = val("100.10").eur().entity();
		bucks.receiveMoney(amountToReceive);
		
		assertEquals("The remain rechage limits is not the expected.", val("2399.90").eur().entity(), bucks.getRemainingRechargeLimit());
	}
	
	@Test
	public void shouldReturnAnIdForMoneyBlock() {
		String blockedId = bucks.blockBalance(val("3.40").eur().entity());
		assertNotNull("The blocked id must be not null", blockedId);
	}

	@Test
	public void shouldCreateMoneyBlock() {
		Money money = val("3.40").eur().entity();
		String blockedId = bucks.blockBalance(money);
		Map<String, Money> blockedBalances = bucks.getBlockedBalances();

		assertNotNull("The blocked balances  must be not null", blockedBalances);
		assertEquals("The blocked balance is not the expected", money, blockedBalances.get(blockedId));
	}

	@Test
	public void shouldReturnTheTotalBlockedAmount() {
		bucks.blockBalance(val("5.40").eur().entity());
		bucks.blockBalance(val("1.30").eur().entity());

		Money blockedBalance = bucks.getBlockedBalance();

		assertNotNull("The blocked balances  must be not null", blockedBalance);
		assertEquals("The blocked balance is not the expected", val("6.70").eur().entity(), blockedBalance);
	}

	@Test
	public void shouldReceiveMoney() {
		bucks.setTotalBalance(val("5.40").eur().entity());
		bucks.receiveMoney(val("3.40").eur().entity());

		Money availableBalance = bucks.getAvailableBalance();

		assertNotNull("The available balances must be not null", availableBalance);
		assertEquals("The available balance is not the expected", val("8.80").eur().entity(), availableBalance);
	}

	@Test
	public void shouldReceiveNegativeMoney(){
		bucks.setTotalBalance(val("10.1").eur().entity());
		bucks.receiveMoney(val("-10").eur().entity());

		Money availableBalance = bucks.getAvailableBalance();

		assertNotNull("The available balances must be not null", availableBalance);
		assertEquals("The available balance is not the expected", val("0.1").eur().entity(), availableBalance);
	}
	
	@Test
	public void shouldWithdrawMoney() {
		bucks.setTotalBalance(val("5.40").eur().entity());
		bucks.withdrawMoney(val("3.40").eur().entity());

		Money availableBalance = bucks.getAvailableBalance();

		assertNotNull("The available balances must be not null", availableBalance);
		assertEquals("The available balance is not the expected", val("2.00").eur().entity(), availableBalance);
	}
	
	@Test
	public void shouldRefundMoneyWhenZeroBalance(){
		bucks.refund(val("20.30").eur().entity());
		
		assertEquals("The total balance must be 20.30", val("20.30").eur().entity(),bucks.getTotalBalance());
	}
	
	@Test
	public void shouldRefundMoneyWhenSomeBalance(){
		bucks.setTotalBalance(val("2.30").eur().entity());
		bucks.refund(val("20.30").eur().entity());
		
		assertEquals("The total balance must be 22.60", val("22.60").eur().entity(),bucks.getTotalBalance());
	}

	@InjectMocks
	private Bucks bucks = new Bucks(CUSTOMER_ID, AGREEMENT_ID);
	
	@Mock
	private BucksRepository bucksRepository;

	@Mock
	private BusinessRulesValidator<Bucks> bucksWithdrawalValidator;

	@Mock
	private BusinessRulesValidator<Bucks> bucksReceptionValidator;

	private static final String CUSTOMER_ID = "a9sd76a7sd6a9s6";

	private static final String AGREEMENT_ID = "ad987d9a75d86a46784f";
}