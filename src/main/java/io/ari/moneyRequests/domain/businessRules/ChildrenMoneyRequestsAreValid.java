package io.ari.moneyRequests.domain.businessRules;

import io.ari.bussinessRules.BusinessRule;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.moneyRequests.domain.MoneyRequest;
import io.ari.moneyRequests.domain.MoneyRequestBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.util.stream.Collectors.toSet;

@Component
public class ChildrenMoneyRequestsAreValid implements BusinessRule<MoneyRequestBundle> {

    @Override
    public Collection<Violation> isSatisfied(MoneyRequestBundle moneyRequestBundle, Object... additionalData) {
        return moneyRequestBundle.getRequests()
                .stream()
                .filter(moneyRequest -> !validate(moneyRequest).isEmpty())
                .map(this::getViolation)
                .collect(toSet());
    }

    private Collection<Violation> validate(MoneyRequest moneyRequest) {
        return businessRulesValidator.validate(moneyRequest);
    }

    private Violation getViolation(MoneyRequest moneyRequest) {
        return new Violation("InvalidMoneyRequests", "The money requests are not valid");
    }

    @Autowired
    @Qualifier("moneyRequestValidator")
    private BusinessRulesValidator<MoneyRequest> businessRulesValidator;
}
