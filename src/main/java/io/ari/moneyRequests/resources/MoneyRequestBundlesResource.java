package io.ari.moneyRequests.resources;

import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.moneyRequests.domain.services.MoneyRequestBundleAppService;
import io.ari.moneyRequests.resources.assemblers.MoneyRequestBundlesAssembler;
import io.ari.repositories.exceptions.EntityNotFound;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.executable.ValidateOnExecution;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("moneyRequests")
public class MoneyRequestBundlesResource {

    @RequestMapping(method = RequestMethod.POST)
    @ValidateOnExecution
    public ResponseEntity createMoneyRequest(@RequestHeader("x-customer-id") @NotEmpty String customerId,
                                             @RequestBody Map<String, Object> requestBundleDto) throws EntityNotFound {
        try {
            MoneyRequestBundle moneyRequestBundle = assembler.convertDtoToEntity(customerId, requestBundleDto);

            Collection<Violation> violations = validator.validate(moneyRequestBundle);
            moneyRequestBundle.setViolations(violations);

            if (!violations.isEmpty()) {
                return resumeConflictedRequest(moneyRequestBundle);
            }

            return returnProcessedRequest(moneyRequestBundleAppService.process(customerId, moneyRequestBundle));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private ResponseEntity returnProcessedRequest(MoneyRequestBundle moneyRequestBundle) {
        return ResponseEntity.accepted().body(assembler.convertEntityToDto(moneyRequestBundle));
    }

    private ResponseEntity resumeConflictedRequest(MoneyRequestBundle moneyRequestBundle) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(assembler.convertEntityToDto(moneyRequestBundle));
    }

    @Autowired
    private MoneyRequestBundlesAssembler assembler;

    @Autowired
    @Qualifier("moneyRequestBundleValidator")
    private BusinessRulesValidator<MoneyRequestBundle> validator;

    @Autowired
    private MoneyRequestBundleAppService moneyRequestBundleAppService;
}