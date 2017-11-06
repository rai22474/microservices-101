package io.ari.moneyRequests.domain.businessRules;

import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bussinessRules.BusinessRule;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import io.ari.repositories.exceptions.EntityNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RequestSenderCanReceive implements BusinessRule<MoneyRequestBundle> {

    @Override
    public Collection<Violation> isSatisfied(MoneyRequestBundle moneyRequestBundle, Object... additionalData) {
        return bucksReceptionValidator.validate(getBucks(moneyRequestBundle), moneyRequestBundle.calculateAmount());
    }

    private Bucks getBucks(MoneyRequestBundle moneyRequestBundle) {
        return bucksRepository.findById(moneyRequestBundle.getBucksId());
    }

    @Autowired
    private BucksRepository bucksRepository;

    @Autowired
    @Qualifier("bucksReceptionValidator")
    private BusinessRulesValidator<Bucks> bucksReceptionValidator;
}
