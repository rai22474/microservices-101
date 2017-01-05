package io.ari.moneyOrders.domain.recipients.repositories.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.bucks.domain.repositories.assemblers.BucksRecipientsStorageAssembler;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.repositories.assemblers.StorageAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RecipientsStorageAssembler extends StorageAssembler<Recipient> {

	public Map<String, Object> convertEntityToDto(Recipient recipient) {
		Map<String, Object> data = new HashMap<>(recipient.getData());
		data.put("type", recipient.getType());

		return ImmutableMap.copyOf(data);
	}

	public Recipient convertDtoToEntity(Map<String, Object> recipientDto) {

		switch ((String) recipientDto.getOrDefault("type", "")) {
			case "bucksRecipient":
				return bucksRecipientsStorageAssembler.convertDtoToDomainEntity(recipientDto, (String) recipientDto.get("targetBucksId"));
			case "accountsRecipient":
				return accountRecipientsStorageAssembler.convertDtoToDomainEntity(recipientDto, (String) recipientDto.get("accountNumber"));
		}

		throw new IllegalStateException();

	}

	@Autowired
	private BucksRecipientsStorageAssembler bucksRecipientsStorageAssembler;

	@Autowired
	private AccountRecipientsStorageAssembler accountRecipientsStorageAssembler;

}
