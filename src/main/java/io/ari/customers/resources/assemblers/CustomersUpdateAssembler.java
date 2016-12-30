package io.ari.customers.resources.assemblers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.ari.customers.domain.Customer;
import io.ari.customers.domain.repositories.CustomersRepository;
import io.ari.repositories.exceptions.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class CustomersUpdateAssembler {

	public Customer convertDtoToExistingEntity(String customerId, Map<String, Object> customerData) throws EntityNotFound {
		Customer existingCustomer = customersRepository.findById(customerId);

        if (customerData.containsKey("name")){
            existingCustomer.setName((String) customerData.get("name"));
        }

        if (customerData.containsKey("lastName")){
            existingCustomer.setLastName((String) customerData.get("lastName"));
        }

        if (customerData.containsKey("email")){
            existingCustomer.setEmail((String) customerData.get("email"));
        }

		if (customerData.containsKey("address")) {
            existingCustomer.setAddress(Maps.filterKeys((Map<String, Object>) customerData.get("address"), addressKeys::contains));
		}

		return existingCustomer;
	}

	private Collection<String> addressKeys = ImmutableList.of(
			"postcode",
			"addressType",
			"streetAddress",
			"streetNumber",
			"streetType",
			"houseNumber",
			"houseLetter",
			"houseStair",
			"country",
			"town");


	@Autowired
	private CustomersRepository customersRepository;

}
