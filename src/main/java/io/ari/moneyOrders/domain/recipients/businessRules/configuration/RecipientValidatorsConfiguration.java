package io.ari.moneyOrders.domain.recipients.businessRules.configuration;

import com.google.common.collect.ImmutableSet;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.moneyOrders.domain.recipients.Recipient;
import io.ari.moneyOrders.domain.recipients.businessRules.BucksFromRecipientCanReceive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecipientValidatorsConfiguration {

    @Bean(name = "bucksRecipientValidator")
    public BusinessRulesValidator<Recipient> bucksRecipientValidator() {
        return new BusinessRulesValidator<>(ImmutableSet.of(
                bucksFromRecipientCanReceive));
    }

    @Autowired
    private BucksFromRecipientCanReceive bucksFromRecipientCanReceive;
}
