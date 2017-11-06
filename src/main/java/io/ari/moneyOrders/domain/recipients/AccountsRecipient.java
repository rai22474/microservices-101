package io.ari.moneyOrders.domain.recipients;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.money.domain.Money;
import io.ari.repositories.exceptions.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.HashMap;
import java.util.Map;

@Configurable(dependencyCheck = true)
public class AccountsRecipient extends Recipient {

    public AccountsRecipient(String targetAccountNumber) {
        super(new HashMap<>());
        this.targetAccountNumber = targetAccountNumber;
        getData().put("accountNumber", targetAccountNumber);
    }

    public AccountsRecipient(@JsonProperty("accountNumber") String targetAccountNumber,
                             @JsonProperty("data") Map<String, Object> data) {
        super(data);
        this.targetAccountNumber = targetAccountNumber;
    }

    @Override
    public String requestMoneyOrder(Bucks sourceBucks, Money amount, String operationId) {
        return "";
        /*return bankingService.transferToAccount(
                sourceBucks.getBankingServiceAgreementId(),
                targetAccountNumber,
                amount,
                operationId);*/
    }

    @Override
    public void confirmMoneyOrder(Bucks sourceBucks, Map<String, Object> event) {

        Money amount = (Money) event.get("amount");

        sourceBucks.withdrawMoney(amount);

        bucksRepository.update(sourceBucks.getId(), sourceBucks);
    }

    @Override
    public String submitMoneyRequest(String sourceBucks, Money amount, String reason, String sourceCommand) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType() {
        return "accountsRecipient";
    }

    @Override
    public boolean isTheSameAs(String bucksId) {
        return bucksRepository.findById(bucksId).getBankingServiceAgreementId().equals(targetAccountNumber);
    }

    @Override
    public Recipient clone() {
        return new AccountsRecipient(targetAccountNumber, getData());
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    private String targetAccountNumber;

    @Autowired
    private BucksRepository bucksRepository;

}
