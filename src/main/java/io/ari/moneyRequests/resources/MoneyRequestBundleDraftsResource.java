package io.ari.moneyRequests.resources;

import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.moneyRequests.domain.repositories.MoneyRequestBundlesRepository;
import io.ari.moneyRequests.resources.assemblers.MoneyRequestBundleDraftsAssembler;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("drafts/moneyRequests")
public class MoneyRequestBundleDraftsResource {

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity createMoneyRequestBundleDraft(@RequestHeader("x-customer-id") @NotEmpty String customerId,
														@RequestBody @NotNull Map<String, Object> moneyRequestBundleDraft) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(processBundleDraft(customerId,
					moneyRequestBundleDraft));
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}

	}

	private Map<String, Object> processBundleDraft(String customerId, Map<String, Object> moneyRequestBundleDraftDto) {
		MoneyRequestBundle savedDraft = repository.save(assembler.convertDtoToEntity(customerId, moneyRequestBundleDraftDto));

		Collection<Violation> violations = validator.validate(savedDraft);
		savedDraft.setViolations(violations);

		return assembler.convertEntityToDto(savedDraft);
	}

	@Autowired
	private MoneyRequestBundleDraftsAssembler assembler;

	@Autowired
	private MoneyRequestBundlesRepository repository;

	@Autowired
	@Qualifier("moneyRequestBundleValidator")
	private BusinessRulesValidator<MoneyRequestBundle> validator;
}
