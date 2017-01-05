package io.ari.bucks.resources;

import com.google.common.collect.ImmutableMap;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.repositories.exceptions.EntityNotFound;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

import static io.ari.money.MoneyBuilder.val;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RechargesResourceTest {

    @Test
    public void shouldReturnTheCreatedResponse() throws EntityNotFound {
        when(bucksRepository.findBucksByCustomerId(CUSTOMER_ID)).thenReturn(bucks);
        ResponseEntity response = rechargesResource.createRecharge(CUSTOMER_ID,
                createRechargeData());

        assertEquals("The response code should be CREATED", 201, response.getStatusCodeValue());
    }

    @Test
    public void shouldUpdateTheCustomerBucks() throws EntityNotFound {
        Map<String,Object> rechargeData = createRechargeData();
        when(moneyAssembler.convertDtoToEntity(rechargeData)).thenReturn(val("10.0").eur().entity());
        when(bucksRepository.findBucksByCustomerId(CUSTOMER_ID)).thenReturn(bucks);

        rechargesResource.createRecharge(CUSTOMER_ID, rechargeData);

        verify(bucks).receiveMoney(val("10.0").eur().entity());
        verify(bucksRepository).save(bucks);
    }

    private Map<String,Object> createRechargeData(){
        return ImmutableMap.of("value", new BigDecimal("10.0"), "currency", "EUR");
    }

    @InjectMocks
    private RechargesResource rechargesResource;

    private static String CUSTOMER_ID = "12345678";

    @Mock
    private BucksRepository bucksRepository;

    @Mock
    private Bucks bucks;

    @Mock
    private MoneyAssembler moneyAssembler;
}