package io.ari.customers.resources;

import io.ari.customers.domain.Customer;
import io.ari.customers.domain.repositories.CustomersRepository;
import io.ari.customers.resources.assemblers.CustomersAssembler;
import io.ari.customers.resources.assemblers.CustomersUpdateAssembler;
import io.ari.repositories.exceptions.EntityNotFound;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.executable.ValidateOnExecution;
import java.util.Map;

@RestController
@RequestMapping("me")
public class CustomerResource {

	@ValidateOnExecution
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity me(@RequestHeader("x-customer-id") @NotEmpty String customerId) {
		try {
			Map<String, Object> customer = customersRepository.findOne(customerId);
			return ResponseEntity.ok(customersAssembler.convertEntityToDto(customer));
		} catch (EntityNotFound e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity deleteMyCustomer(@RequestHeader("x-customer-id") String customerId) throws EntityNotFound {
		customersRepository.delete(customerId);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity updateCustomer(@RequestHeader("x-customer-id") String customerId,
							   @RequestBody Map<String, Object> customerData) throws EntityNotFound {
		Customer updatedCustomer = customersAddressAssembler.convertDtoToExistingEntity(customerId, customerData);
		customersRepository.update(customerId, updatedCustomer);

		return ResponseEntity.noContent().build();
	}

	@Autowired
	private CustomersUpdateAssembler customersAddressAssembler;

	@Autowired
	private CustomersRepository customersRepository;

	@Autowired
	private CustomersAssembler customersAssembler;

}