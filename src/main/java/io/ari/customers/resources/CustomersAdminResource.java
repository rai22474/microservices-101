package io.ari.customers.resources;

import io.ari.repositories.write.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("customers")
public class CustomersAdminResource {

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity createCustomer(@RequestBody Map<String, Object> customerData) {
		Map<String,Object> savedEntity = customersRepository.saveEntity(customerData);

		Map<String, Object> entity = new HashMap<>();
		entity.put(ENTITY_ID, savedEntity.get("id"));

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(entity);
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity deleteCustomers() {
		customersRepository.deleteAll();
		return ResponseEntity.noContent().build();
	}

	private static final String ENTITY_ID = "entityId";

	@Autowired
	@Qualifier("customersRepository")
	private EntityRepository customersRepository;
}
