package io.ari.bucks.resources;

import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bucks.resources.assemblers.BucksAssembler;
import io.ari.repositories.exceptions.EntityNotFound;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.executable.ValidateOnExecution;

@RestController
@RequestMapping("bucks")
public class BucksResource {

	@RequestMapping(method = RequestMethod.GET)
	@ValidateOnExecution
	public ResponseEntity findBucks(@RequestHeader("x-customer-id") @NotEmpty String customerId) {

		try {
			return ResponseEntity.ok().body(bucksAssembler.convertEntitiesToDto(bucksRepository.findByCustomerId(customerId)));
		} catch (EntityNotFound entityNotFound) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@Autowired
	private BucksRepository bucksRepository;
	
	@Autowired
	private BucksAssembler bucksAssembler;
}
