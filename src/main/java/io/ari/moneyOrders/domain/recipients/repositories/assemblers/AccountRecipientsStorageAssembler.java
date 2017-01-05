package io.ari.moneyOrders.domain.recipients.repositories.assemblers;

import io.ari.moneyOrders.domain.recipients.AccountsRecipient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AccountRecipientsStorageAssembler {

    public AccountsRecipient convertDtoToDomainEntity(Map<String, Object> recipientDto, String bucksId) {
        AccountsRecipient recipient = new AccountsRecipient(bucksId);
        recipient.putData(recipientDto);
        return recipient;
    }
}
