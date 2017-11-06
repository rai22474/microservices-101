package io.ari.bucks.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.assemblers.HypermediaAssembler;
import io.ari.bucks.domain.Bucks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.ari.money.MoneyBuilder.val;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BucksAssemblerTest {

    @Test
    public void shouldNotReturnNullDto() {
        Bucks bucks = createBucks();

        Map<String, Object> bucksDto = bucksAssembler.convertEntitiesToDto(bucks);

        assertNotNull("The bucks must be not null", bucksDto);
    }

    @Test
    public void shouldNotReturnMustHaveBalanceDto() {
        Bucks bucks = createBucks();

        when(moneyAssembler.convertEntityToDto(val("10.0").eur().entity())).thenReturn(ImmutableMap.of("value", new BigDecimal(10),
                "currency","EUR"));

        Map<String, Object> bucksDto = bucksAssembler.convertEntitiesToDto(bucks);
        Map<String, Object> balance = (Map<String, Object>) bucksDto.get("balance");

        assertNotNull("The bucks must be not null", balance);
        assertEquals("the total balance is not the expected", createMoney(TOTAL_TEST), balance.get("total"));
        assertEquals("the available balance is not the expected", createMoney(AVAILABLE_TEST), balance.get("available"));
    }

    @Test
    public void shouldNotReturnMustHaveLimitsDto() {
        Bucks bucks = createBucks();

        when(moneyAssembler.convertEntityToDto(val("10.0").eur().entity()))
                .thenReturn(ImmutableMap.of("value", new BigDecimal(10), "currency","EUR"));

        Map<String, Object> bucksDto = bucksAssembler.convertEntitiesToDto(bucks);

        Map<String, Object> limits = (Map<String, Object>) bucksDto.get("limits");

        assertNotNull("The bucks limits must be not null", limits);
        assertEquals("the recharge limit is not the expected", createMoney(RECHARGE_LIMIT_TEST), limits.get("rechargedThisPeriod"));
        assertEquals("The total is not the expected", createMoney(TOTAL_LIMIT_TEST), limits.get("total"));
    }

    @Test
    public void shouldNotHaveParticipants() {
        Bucks bucks = createBucks();

        Map<String, Object> bucksDto = bucksAssembler.convertEntitiesToDto(bucks);

        Object participants = bucksDto.get("participants");
        assertNull("The participant must be null", participants);
    }

    @Test
    public void shouldHaveAListOfLinks() {
        Bucks bucks = createBucks();

        Map<String, Object> bucksLink = new HashMap<>();
        Map<String, Object> hypermedia = new HashMap<>();
        hypermedia.put("self", bucksLink);

        when(hypermediaAssembler.createHypermedia("api/bucks", "ari-read")).thenReturn(hypermedia);

        Map<String, Object> bucksDto = bucksAssembler.convertEntitiesToDto(bucks);
        Map<String, Object> links = (Map<String, Object>) bucksDto.get("_links");

        assertNotNull("The map of links must not be null", links);
        assertEquals("Hypermedia is not the expected", hypermedia, links);
    }

    @Test
    public void shouldNotHaveAListOfMoneyOrders() {
        Bucks bucks = createBucks();

        Map<String, Object> bucksDto = bucksAssembler.convertEntitiesToDto(bucks);

        assertNull("There no moneyOrderSpec collection", bucksDto.get("moneyOrderSpecs"));
    }

    @Test
    public void shouldNotHaveAListOfMoneyOrderDrafts() {
        Bucks bucks = createBucks();

        Map<String, Object> bucksDto = bucksAssembler.convertEntitiesToDto(bucks);

        assertNull("There no moneyOrderSpec collection", bucksDto.get("moneyOrderSpecDrafts"));
    }

    @Test
    public void shouldFindTheCustomerBucks() {
        Map<String, Object> entityKey = bucksAssembler.createEntityKey(CUSTOMER_ID);

        assertNotNull("The entity key of a customer must not be null", entityKey);
        assertEquals("The entity key must be the expected", CUSTOMER_ID, entityKey.get("participants.id"));
        assertEquals("The entity key must be the expected", "bucks", entityKey.get("type"));
    }

    private Bucks createBucks() {
        when(bucks.getTotalBalance()).thenReturn(val("10.0").eur().entity());
        when(bucks.getAvailableBalance()).thenReturn(val("10.0").eur().entity());
        when(bucks.getThisPeriodRechargeLimit()).thenReturn(val("10.0").eur().entity());
        when(bucks.getMaxRechargeLimit()).thenReturn(val("10.0").eur().entity());

        return bucks;
    }

    private Map<String, Object> createMoney(String money) {
        String[] splitMoney = money.split(" ");

        return ImmutableMap.of("value", new BigDecimal(splitMoney[0]),
                "currency", splitMoney[1]);
    }

    @InjectMocks
    private BucksAssembler bucksAssembler;

    @Mock
    private HypermediaAssembler hypermediaAssembler;

    @Mock
    private Bucks bucks;

    @Mock
    private MoneyAssembler moneyAssembler;

    private String CUSTOMER_ID = "5366bc483004a446a0b49dad";

    private String AVAILABLE_TEST = "10 EUR";

    private String TOTAL_TEST = "10 EUR";

    private String RECHARGE_LIMIT_TEST = "10 EUR";

    private String TOTAL_LIMIT_TEST = "10 EUR";

}

