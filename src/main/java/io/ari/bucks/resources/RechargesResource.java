package io.ari.bucks.resources;

import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("recharges")
public class RechargesResource {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createRecharge(@RequestHeader("x-customer-id") String customerId,
                                         @RequestBody Map<String, Object> rechargeData) {
        Bucks bucks = bucksRepository.findBucksByCustomerId(customerId);
        bucks.receiveMoney(moneyAssembler.convertDtoToEntity(rechargeData));
        bucksRepository.save(bucks);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Autowired
    private BucksRepository bucksRepository;

    @Autowired
    private MoneyAssembler moneyAssembler;
}
