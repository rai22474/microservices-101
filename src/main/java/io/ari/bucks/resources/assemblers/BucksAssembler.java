package io.ari.bucks.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.assemblers.HypermediaAssembler;
import io.ari.bucks.domain.Bucks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BucksAssembler {

    public Map<String, Object> convertEntitiesToDto(Bucks bucks) {
        return ImmutableMap.of("balance", createBalance(bucks),
                "limits", createLimits(bucks),
                "_links", hypermediaAssembler.createHypermedia(SELF_URI, "ari-read"));
    }

    public Map<String, Object> createEntityKey(String customerId) {
        Map<String, Object> entityKey = new HashMap<>();
        entityKey.put("participants.id", customerId);
        entityKey.put("type", BUCKS);
        return entityKey;
    }

    private Map<String, Object> createBalance(Bucks bucks) {
        return ImmutableMap.of("available", moneyAssembler.convertEntityToDto(bucks.getAvailableBalance()),
                "total", moneyAssembler.convertEntityToDto(bucks.getTotalBalance()));
    }

    private Map<String, Object> createLimits(Bucks bucks) {
        return ImmutableMap.of("rechargedThisPeriod", moneyAssembler.convertEntityToDto(bucks.getThisPeriodRechargeLimit()),
                "total", moneyAssembler.convertEntityToDto(bucks.getMaxRechargeLimit()),
                "recharge", createRechargeLimits(bucks));
    }

    private Map<String, Object> createRechargeLimits(Bucks bucks) {
        Map<String, Object> recharge = new HashMap<>();

        recharge.put("thisPeriod", moneyAssembler.convertEntityToDto(bucks.getThisPeriodRechargeLimit()));
        recharge.put("last", moneyAssembler.convertEntityToDto(bucks.getLastRecharge()));
        recharge.put("remaining", moneyAssembler.convertEntityToDto(bucks.getRemainingRechargeLimit()));
        recharge.put("max", moneyAssembler.convertEntityToDto(bucks.getMaxRechargeLimit()));

        return recharge;
    }

    private static final String SELF_URI = "api/bucks";

    private static final String BUCKS = "bucks";

    @Autowired
    private HypermediaAssembler hypermediaAssembler;

    @Autowired
    private MoneyAssembler moneyAssembler;

}
