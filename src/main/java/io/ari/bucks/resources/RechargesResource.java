package io.ari.bucks.resources;

import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bucks.resources.assemblers.MoneyAssembler;
import io.ari.repositories.exceptions.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("recharges")
public class RechargesResource {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createRecharge(@RequestHeader("x-customer-id")String customerId,
                                         @RequestBody Map<String, Object> rechargeData) {
        try {
            Bucks bucks = bucksRepository.findBucksByCustomerId(customerId);
            bucks.receiveMoney(moneyAssembler.convertDtoToEntity(rechargeData));
            bucksRepository.save(bucks);
        } catch (EntityNotFound entityNotFound) {
            entityNotFound.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Autowired
    private BucksRepository bucksRepository;

    @Autowired
    private MoneyAssembler moneyAssembler;
}
