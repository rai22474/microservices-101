package io.ari.bucks.resources.assemblers;

import com.google.common.collect.Maps;
import io.ari.moneyOrders.domain.recipients.BucksRecipient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BucksRecipientsAssembler {

	public BucksRecipient convertDtoToEntity(Map<String, Object> recipientData, String bucksId) {
		BucksRecipient recipient = new BucksRecipient(bucksId);

		Map<String, Object> additionalData = Maps.newHashMap(recipientData);
		additionalData.put("targetBucksId", bucksId);
		recipient.putData(additionalData);

		return recipient;
	}
}
