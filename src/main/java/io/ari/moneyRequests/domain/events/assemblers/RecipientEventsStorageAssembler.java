package io.ari.moneyRequests.domain.events.assemblers;

import com.google.common.collect.ImmutableMap;
import io.ari.moneyOrders.domain.recipients.Recipient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RecipientEventsStorageAssembler {

	public Map<String, Object> convertDomainEntityToDto(Recipient recipient) {
		return ImmutableMap.copyOf(recipient.getData());
	}

}
