package io.ari.bucks.domain.repositories.assemblers;

import io.ari.moneyOrders.domain.recipients.BucksRecipient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BucksRecipientsStorageAssembler {

	public BucksRecipient convertDtoToDomainEntity(Map<String, Object> recipientDto, String bucksId) {
		BucksRecipient recipient = new BucksRecipient(bucksId);
		recipient.putData(recipientDto);
		return recipient;
	}

}
