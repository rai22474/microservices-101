package io.ari.moneyRequests.domain;

import io.ari.money.domain.Money;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyRequestBundleTest {

    @Test
    public void shouldReturnNotNullWhenCalculateAmountWithNoRequest(){
        MoneyRequestBundle moneyRequestBundle = new MoneyRequestBundle(ID, CREATION_DATE, TEST_BUCKS_ID, new MoneyRequest[]{});

        Money calculatedAmount = moneyRequestBundle.calculateAmount();

        assertNotNull("The calculated amount must be not null", calculatedAmount);
    }

    @Test
    public void shouldReturnTheAmountOfTheRequestWhenThereAreOnlyOne(){
        when(firstMoneyRequest.getAmount()).thenReturn(new Money(new BigDecimal("10.00"), "EUR"));
        MoneyRequestBundle moneyRequestBundle = new MoneyRequestBundle(ID, CREATION_DATE, TEST_BUCKS_ID, new MoneyRequest[]{firstMoneyRequest});

        Money calculatedAmount = moneyRequestBundle.calculateAmount();

        assertEquals("The calculated amount must be 10.00", new Money(new BigDecimal("10.00"), "EUR"),calculatedAmount);
    }

    private static final String TEST_BUCKS_ID = "60c77f79bcf95cd7894f6c0e";

    private static final String ID = "d89a6da46da3a0da";

    private static final Date CREATION_DATE = new Date();

    @Mock(name = "firstMoneyRequest")
    private MoneyRequest firstMoneyRequest;
}
