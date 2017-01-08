package io.ari.moneyRequests.resources;

import io.ari.repositories.exceptions.EntityNotFound;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.executable.ValidateOnExecution;

@RestController
@RequestMapping("moneyRequests/{draftId}")
public class MoneyRequestBundleResource {

	@RequestMapping(method = RequestMethod.DELETE)
	@ValidateOnExecution
	public ResponseEntity deleteMoneyRequest(@PathVariable("draftId") String draftId) {
		return ResponseEntity.noContent().build();
	}

}