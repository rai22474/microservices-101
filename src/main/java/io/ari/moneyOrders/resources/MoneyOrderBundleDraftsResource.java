package io.ari.moneyOrders.resources;

import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import io.ari.moneyOrders.domain.repositories.MoneyOrderBundlesRepository;
import io.ari.moneyOrders.resources.assemblers.MoneyOrderBundleDraftsAssembler;
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
@RequestMapping("drafts/moneyOrders")
public class MoneyOrderBundleDraftsResource {

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity createMoneyOrderBundleDraft(@RequestHeader("x-customer-id") @NotEmpty String customerId,
													  @RequestBody @NotNull Map<String, Object> moneyOrderBundleDraftDto){
		try {
			MoneyOrderBundle moneyOrderBundleDraft = processDraftCommand(customerId, moneyOrderBundleDraftDto);
			Map<String, Object> publishedDraftDto = assembler.convertEntityToDto(moneyOrderBundleDraft);

			return ResponseEntity
					.status(HttpStatus.CREATED)
					.body(publishedDraftDto);
		} catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}

	private MoneyOrderBundle processDraftCommand(String customerId, Map<String, Object> moneyOrderBundleDraftDto) {
		MoneyOrderBundle moneyOrderBundle = repository.save(assembler.convertDtoToEntity(customerId, moneyOrderBundleDraftDto));

		Collection<Violation> violations = validator.validate(moneyOrderBundle);
		moneyOrderBundle.setViolations(violations);
		return moneyOrderBundle;
	}

	@Autowired
	private MoneyOrderBundleDraftsAssembler assembler;

	@Autowired
	private MoneyOrderBundlesRepository repository;

	@Autowired
	@Qualifier("moneyOrderBundleValidator")
	private BusinessRulesValidator<MoneyOrderBundle> validator;
}
