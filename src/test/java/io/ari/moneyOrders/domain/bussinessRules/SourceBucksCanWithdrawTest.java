package io.ari.moneyOrders.domain.bussinessRules;

import com.google.common.collect.ImmutableList;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.moneyOrders.domain.businessRules.SourceBucksCanWithdraw;
import io.ari.repositories.exceptions.EntityNotFound;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SourceBucksCanWithdrawTest {

	@Test
	public void shouldBeSatisfiedWhenAmountCanBeWithdrawnFromBucks() {
		when(bucksWithdrawalValidator.validate(bucks,money)).thenReturn(ImmutableList.of());

		assertTrue(rule.isSatisfied(moneyOrderBundle).isEmpty());
	}

	@Test
	public void shouldNotBeSatisfiedWhenAmountCannotBeWithdrawnFromBucks() {
		when(bucksWithdrawalValidator.validate(bucks,money)).thenReturn(ImmutableList.of(mock(Violation.class)));
		assertFalse(rule.isSatisfied(moneyOrderBundle).isEmpty());
	}

	@Before
	public void setup() throws EntityNotFound {
		when(moneyOrderBundle.calculateAmount()).thenReturn(money);

		when(moneyOrderBundle.getBucksId()).thenReturn(BUCKS_ID);

		when(bucksRepository.findById(BUCKS_ID)).thenReturn(bucks);
	}

	@InjectMocks
	private SourceBucksCanWithdraw rule;

	@Mock
	private MoneyOrderBundle moneyOrderBundle;

	@Mock
	private BucksRepository bucksRepository;

	@Mock
	private Bucks bucks;

	@Mock
	private Money money;

	@Mock
	private BusinessRulesValidator<Bucks> bucksWithdrawalValidator;

	private static final String BUCKS_ID = "ad9769a76d97ad9a";

}