package io.ari.customers.resources;

import io.ari.repositories.write.EntityRepository;
import io.ari.customers.resources.assemblers.CustomersAdminAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("customers/{customerId}")
public class CustomerAdminResource {

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity deleteClient(@PathVariable("customerId") String customerId) {
		customersRepository.delete(customerId);
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity updateClient(@PathVariable("customerId")  String customerId, @RequestBody Map<String, Object> customerData) {
		Map<String, Object> entity = customersAssembler.createEntityFromDto(customerData);
		customersRepository.update(customerId,entity);
		return ResponseEntity.ok().build();
	}
	
	@Autowired
	@Qualifier("customersRepository")
	private EntityRepository customersRepository;
	
	@Autowired
	private CustomersAdminAssembler customersAssembler;
}
