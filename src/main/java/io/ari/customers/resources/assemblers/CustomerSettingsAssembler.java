package io.ari.customers.resources.assemblers;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.ari.customers.domain.Customer;
import io.ari.customers.domain.repositories.CustomersRepository;
import io.ari.repositories.exceptions.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class CustomerSettingsAssembler {

	public Customer convertDtoToExistingEntity(String customerId, Map<String, Object> customerData) throws EntityNotFound {
		Customer existingCustomer = customersRepository.findById(customerId);
		Map<String, Object> notifications = Maps.filterKeys((Map<String, Object>) customerData.get("notifications"), settingsKeys::contains);
		existingCustomer.setSettings(ImmutableMap.of("notifications", notifications));
		return existingCustomer;
	}

	private Collection<String> settingsKeys = ImmutableList.of(
			"tooltips",
			"alerts",
			"news",
			"coinJars",
			"pots",
			"moneyRequests",
			"moneyOrders");

	@Autowired
	private CustomersRepository customersRepository;
}
