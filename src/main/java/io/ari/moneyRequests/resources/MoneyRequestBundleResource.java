package io.ari.moneyRequests.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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