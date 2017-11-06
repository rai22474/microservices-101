package io.ari.moneyRequests.resources;

import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.moneyRequests.domain.repositories.MoneyRequestBundlesRepository;
import io.ari.moneyRequests.resources.assemblers.MoneyRequestBundleDraftsAssembler;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.executable.ValidateOnExecution;
import java.util.Collection;
import java.util.Map;


@RestController
@RequestMapping("drafts/moneyRequests/{draftId}")
public class MoneyRequestBundleDraftResource {

	@RequestMapping(method = RequestMethod.PUT)
	@ValidateOnExecution
	public ResponseEntity modifyMoneyRequestBundleDraft(@RequestHeader("x-customer-id") @NotEmpty String customerId,
														@PathVariable("draftId") String draftId,
														@RequestBody Map<String, Object> moneyRequestBundleDraft)  {
		return ResponseEntity.ok(modifyExistingDraft(customerId,draftId,moneyRequestBundleDraft));
	}

	private Map<String, Object> modifyExistingDraft(String customerId, String draftId, Map<String, Object> moneyRequestBundleDraft) {
		MoneyRequestBundle newRequest = assembler.convertDtoToEntity(customerId, moneyRequestBundleDraft);

		repository.update(draftId, newRequest);
		Collection<Violation> violations = validator.validate(newRequest);
		newRequest.setViolations(violations);

		return assembler.convertEntityToDto(newRequest);
	}

	@Autowired
	private MoneyRequestBundlesRepository repository;

	@Autowired
	private MoneyRequestBundleDraftsAssembler assembler;

	@Autowired
	@Qualifier("moneyRequestBundleValidator")
	private BusinessRulesValidator<MoneyRequestBundle> validator;

}
