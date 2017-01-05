package io.ari.bucks.resources.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.customers.domain.Customer;
import io.ari.customers.domain.repositories.CustomersRepository;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.repositories.exceptions.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RecipientsAssembler {

	public Map<String, Object> convertEntityToDto(Recipient recipient) {
		return ImmutableMap.copyOf(recipient.getData());
	}

	public Recipient convertDtoToEntity(Map<String, Object> recipientDto) {
		if(!recipientDto.containsKey("accountNumber")) {
			return customersRepository.findByMobilePhone((String) recipientDto.get("mobilePhone"))
					.<Recipient>map(customer -> {
						try {
							return bucksRecipientsAssembler.convertDtoToEntity(recipientDto, getCustomerBucks(customer).getId());
						} catch (EntityNotFound entityNotFound) {
							throw new IllegalStateException(entityNotFound);
						}
					}).get();
		} 
	
		return accountsRecipientAssembler.convertDtoToEntity(recipientDto);
	}

	private Bucks getCustomerBucks(Customer customer) throws EntityNotFound {
		return bucksRepository.findBucksByCustomerId(customer.getId());
	}

	@Autowired
	private BucksRecipientsAssembler bucksRecipientsAssembler;

	@Autowired
	private CustomersRepository customersRepository;

	@Autowired
	private BucksRepository bucksRepository;

	@Autowired
	private AccountsRecipientsAssembler accountsRecipientAssembler;

}
