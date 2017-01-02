package io.ari.customers.domain.factories;

import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.factories.BucksFactory;
import io.ari.customers.domain.Customer;
import io.ari.customers.domain.exceptions.CustomerExists;
import io.ari.customers.domain.exceptions.CustomerIdCardExists;
import io.ari.customers.domain.exceptions.CustomerIdExists;
import io.ari.customers.domain.exceptions.CustomerMobilePhoneExists;
import io.ari.customers.domain.repositories.CustomersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomersFactory {

	public Customer createCustomer(String id, String idCard, String name, String lastName,String mobilePhone) throws CustomerExists {
		verifyNonExistingCustomer(id, idCard,mobilePhone);

		Customer newCustomer = createNewCustomer(id, idCard, mobilePhone, name, lastName);
		createNewBucks(id, idCard, mobilePhone, name, lastName);

		return newCustomer;
	}

	private Customer createNewCustomer(String id, String idCard, String mobilePhone, String name, String lastName)  {
		Customer customer = new Customer(id, idCard);
		customer.setName(name);
		customer.setLastName(lastName);
		customer.setMobilePhone(mobilePhone);

		return customersRepository.save(customer);
	}

	private Bucks createNewBucks(String customerId, String idCard, String mobilePhone, String customerName, String customerLastName) {
		return bucksFactory.createBucks(customerId, idCard, mobilePhone, customerName, customerLastName);
	}

	private void verifyNonExistingCustomer(String customerId, String idCard,String mobilePhone) throws CustomerExists {
		verifyNonExistingCustomerId(customerId);
		verifyNonExistingIdCard(idCard);
		verifyNonExistingMobilePhone(mobilePhone);
	}

	private void verifyNonExistingCustomerId(String customerId) throws CustomerIdExists {
		if (customersRepository.exists(customerId)) {
			throw new CustomerIdExists(customerId);
		}
	}

	private void verifyNonExistingIdCard(String idCard) throws CustomerIdCardExists {
		if (customersRepository.findByIdCard(idCard).isPresent()) {
			throw new CustomerIdCardExists(idCard);
		}
	}
	private void verifyNonExistingMobilePhone(String mobilePhone) throws CustomerMobilePhoneExists {
		if (customersRepository.findByMobilePhone(mobilePhone).isPresent()) {
			throw new CustomerMobilePhoneExists(mobilePhone);
		}
	}
	@Autowired
	private CustomersRepository customersRepository;

	@Autowired
	private BucksFactory bucksFactory;
}