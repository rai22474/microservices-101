package io.ari.moneyOrders.resources;

import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.moneyOrders.domain.repositories.MoneyOrderBundlesRepository;
import io.ari.moneyOrders.resources.assemblers.MoneyOrderBundleDraftsAssembler;
import io.ari.repositories.exceptions.EntityNotFound;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.executable.ValidateOnExecution;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("drafts/moneyOrders/{draftId}")
public class MoneyOrderBundleDraftResource {

    @ValidateOnExecution
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity modifyMoneyOrderBundleDraft(@RequestHeader("x-customer-id") @NotEmpty String customerId,
                                                      @PathVariable("draftId") String draftId,
                                                      @RequestBody Map<String, Object> moneyOrderBundleDraftDto) throws EntityNotFound {

        try {
            MoneyOrderBundle moneyOrderBundle = modifyExistingDraft(customerId, draftId, moneyOrderBundleDraftDto);
            Map<String, Object> savedMoneyOrderBundleDto = assembler.convertEntityToDto(moneyOrderBundle);

            return ResponseEntity.ok(savedMoneyOrderBundleDto);
        } catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    private MoneyOrderBundle modifyExistingDraft(String customerId, String draftId, Map<String, Object> moneyOrderBundleDraft) throws EntityNotFound {
        MoneyOrderBundle newDraft = assembler.convertDtoToExistingEntity(customerId, draftId, moneyOrderBundleDraft);
        repository.update(draftId, newDraft);

        Collection<Violation> violations = validator.validate(newDraft);
        newDraft.setViolations(violations);

        return newDraft;
    }

    @Autowired
    @Qualifier("moneyOrderBundleValidator")
    private BusinessRulesValidator<MoneyOrderBundle> validator;

    @Autowired
    private MoneyOrderBundlesRepository repository;

    @Autowired
    private MoneyOrderBundleDraftsAssembler assembler;

}
