package io.ari.customers.resources;

import io.ari.customers.resources.assemblers.CustomersAssembler;
import io.ari.repositories.exceptions.EntityNotFound;
import io.ari.repositories.write.EntityRepository;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.executable.ValidateOnExecution;
import java.util.Map;

@RestController
@RequestMapping("me")
public class CustomerResource {

	@ValidateOnExecution
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity me(@RequestHeader("x-customer-id") @NotEmpty String customerId) {
		return findCustomerById(customerId);
	}

	private ResponseEntity findCustomerById(String customerId) {
		try {
			Map<String, Object> customer = customersRepository.findOne(customerId);
			System.out.println(customer);
			return ResponseEntity.ok(customersAssembler.convertEntityToDto(customer));
		} catch (EntityNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@Autowired
	@Qualifier("subjectsRepository")
	private EntityRepository customersRepository;

	@Autowired
	private CustomersAssembler customersAssembler;

}