package io.ari.customers.resources;

import io.ari.customers.domain.Customer;
import io.ari.customers.domain.repositories.CustomersRepository;
import io.ari.customers.resources.assemblers.CustomerSettingsAssembler;
import io.ari.repositories.exceptions.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("settings")
public class SettingsResource {

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity updateSettings(@RequestHeader("x-customer-id") String customerId,
							   @RequestBody Map<String, Object> settings) throws EntityNotFound {
		Customer customer = customerSettingsAssembler.convertDtoToExistingEntity(customerId,settings);
		customersRepository.update(customerId, customer);
		return ResponseEntity.noContent().build();
	}

	@Autowired
	private CustomersRepository customersRepository;

	@Autowired
	private CustomerSettingsAssembler customerSettingsAssembler;
}
