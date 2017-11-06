package io.ari.moneyOrders.domain.businessRules;

import io.ari.bucks.domain.Bucks;
import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.bussinessRules.BusinessRule;
import io.ari.bussinessRules.BusinessRulesValidator;
import io.ari.bussinessRules.Violation;
import io.ari.money.domain.Money;
import io.ari.moneyOrders.domain.MoneyOrderBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SourceBucksCanWithdraw implements BusinessRule<MoneyOrderBundle> {

    @Override
    public java.util.Collection<Violation> isSatisfied(MoneyOrderBundle moneyOrderBundle, Object... additionalData) {
        Money amount = moneyOrderBundle.calculateAmount();
        return canWithdraw(getBucks(moneyOrderBundle), amount);
    }

    private Bucks getBucks(MoneyOrderBundle moneyOrderBundle) {
        return bucksRepository.findById(moneyOrderBundle.getBucksId());
    }

    public Collection<Violation> canWithdraw(Bucks bucks, Money amount) {
        return bucksWithdrawalValidator.validate(bucks, amount);
    }

    @Autowired
    private BucksRepository bucksRepository;

    @Autowired
    @Qualifier("bucksWithdrawalValidator")
    private BusinessRulesValidator<Bucks> bucksWithdrawalValidator;
}
