package io.ari.bucks.resources.assemblers;

import com.google.common.collect.Maps;
import io.ari.moneyOrders.domain.recipients.AccountsRecipient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AccountsRecipientsAssembler {

    public AccountsRecipient convertDtoToEntity(Map<String, Object> recipientData) {
        AccountsRecipient recipient = new AccountsRecipient((String)recipientData.get("accountNumber"));

        Map<String, Object> additionalData = Maps.newHashMap(recipientData);
        recipient.putData(additionalData);

        return recipient;
    }

}
